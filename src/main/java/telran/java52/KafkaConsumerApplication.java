package telran.java52;

import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import telran.java52.pulse.dto.PulseDto;

@SpringBootApplication
public class KafkaConsumerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(KafkaConsumerApplication.class, args);
	}
	
//	@Bean
//	 Consumer<String> log(){
//		return data-> System.out.println(data);
//	}

	@Bean
     Supplier<PulseDto> pulseDtoSupplier() {
        return () -> new PulseDto(1, System.currentTimeMillis(), 123);
    }

    @Bean
     BiFunction<Integer, Integer, PulseDto> pulseDtoBiFunction() {
        return (id, payload) -> new PulseDto(id, System.currentTimeMillis(), payload);
    }

    @Bean
    Consumer<PulseDto> logConsumer() {
        return System.out::println;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter command (send, generate, generateAndSend, exit): ");
            String command = scanner.nextLine();

            if (command.equals("exit")) {
                break;
            }

            switch (command) {
                case "send":
                    System.out.println("Enter id: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.println("Enter payload: ");
                    int payload = Integer.parseInt(scanner.nextLine());
                    PulseDto pulseDto = new PulseDto(id, System.currentTimeMillis(), payload);
                    logConsumer().accept(pulseDto);
                    break;

                case "generate":
                    PulseDto generatedPulse = pulseDtoSupplier().get();
                    logConsumer().accept(generatedPulse);
                    break;

                case "generateAndSend":
                    System.out.println("Enter id: ");
                    int genId = Integer.parseInt(scanner.nextLine());
                    System.out.println("Enter payload: ");
                    int genPayload = Integer.parseInt(scanner.nextLine());
                    PulseDto genPulseDto = pulseDtoBiFunction().apply(genId, genPayload);
                    logConsumer().accept(genPulseDto);
                    break;

                default:
                    System.out.println("Unknown command. Please try again.");
                    break;
            }
        }

        scanner.close();
    }
}