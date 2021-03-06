package coding.json.practice.batch.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DeciderJobConfig {

    // step과 decider(분기로직) 분리
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    // @Bean
    public Job deciderJob(){
        return jobBuilderFactory.get("stepDeciderJob")
                .start(startStep())
                .next(decider())
                .from(decider())
                    .on("ODD")
                    .to(oddStep())
                .from(decider())
                    .on("EVEN")
                    .to(evenStep())
                .end()
                .build();
    }

    @Bean
    public Step startStep(){
        return stepBuilderFactory.get("startStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>> Start!");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step evenStep(){
        return stepBuilderFactory.get("evenStep")
                .tasklet((contribution, chunkStep) -> {
                    log.info(">>>> 짝수입니다.");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step oddStep(){
        return stepBuilderFactory.get("oddStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>> 홀수입니다.");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public JobExecutionDecider decider(){ // JobExecutionDecider
        return new OddDecider();
    }

    public static class OddDecider implements JobExecutionDecider{
        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution){
            Random rand = new Random();

            int randomNumber = rand.nextInt(50) + 1;
            log.info("랜덤숫자: {}", randomNumber);

            if(randomNumber % 2 == 0){
                return new FlowExecutionStatus("EVEN");
            }else{
                return new FlowExecutionStatus("ODD");
            }
        }
    }
}
