package fr.jfeu.test.sb;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableBatchProcessing
@PropertySource("classpath:application.properties")
public class BatchConfiguration {

	static Logger logger = Logger.getLogger(BatchConfiguration.class);

	@Value("${author}")
	private String auteur;

	@Autowired
	private Environment env;

	@Bean
	public ItemReader<CustomPojo> reader() {
		logger.info("Reading...");
		FlatFileItemReader<CustomPojo> reader = new FlatFileItemReader<CustomPojo>();
		reader.setResource(new ClassPathResource("test.csv"));
		reader.setLineMapper(new DefaultLineMapper<CustomPojo>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "str" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<CustomPojo>() {
					{
						setTargetType(CustomPojo.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public ItemProcessor<CustomPojo, CustomPojo> processor() {
		return new CustomItemProcessor();
	}

	@Bean
	public ItemWriter writer() {
		return new CustomItemWriter();
	}

	@Bean
	public Job job1(JobBuilderFactory jobs, Step step1, Step step2) {
		logger.info("Job 1 - Auteur : " + auteur);
		logger.info("DB host : " + env.getProperty("db.host"));
		logger.info("DB port : " + env.getProperty("db.port"));
		return jobs.get("job1").incrementer(new RunIdIncrementer()).flow(step2).end().build();
	}

	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<CustomPojo> reader, ItemWriter writer, ItemProcessor processor) {
		/* it handles bunches of 10 units */
		return stepBuilderFactory.get("step1").chunk(10).reader(reader).processor(processor).writer(writer).build();
	}

	@Bean
	public Step step2(StepBuilderFactory stepBuilderFactory, ItemReader<CustomPojo> reader, ItemWriter writer, ItemProcessor processor) {
		/* it handles bunches of 10 units */
		return stepBuilderFactory.get("step2").partitioner("step1", new Partitioner() {

			public Map<String, ExecutionContext> partition(int gridSize) {
				Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();

				int range = 10;
				int fromId = 1;
				int toId = range;

				for (int i = 1; i <= gridSize; i++) {
					ExecutionContext value = new ExecutionContext();

					System.out.println("\nStarting : Thread" + i);
					System.out.println("fromId : " + fromId);
					System.out.println("toId : " + toId);

					value.putInt("fromId", fromId);
					value.putInt("toId", toId);

					// give each thread a name, thread 1,2,3
					value.putString("name", "Thread" + i);

					result.put("partition" + i, value);

					fromId = toId + 1;
					toId += range;

				}

				return result;
			}
		}).gridSize(5).taskExecutor(new SimpleAsyncTaskExecutor()).build();
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

}
