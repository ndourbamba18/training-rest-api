package com.parlonsdev;

import com.parlonsdev.entities.Student;
import com.parlonsdev.entities.Training;
import com.parlonsdev.repository.StudentRepository;
import com.parlonsdev.repository.TrainingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Date;

@SpringBootApplication
@EnableJpaAuditing
public class TrainingRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainingRestApiApplication.class, args);
	}


	@Bean
	CommandLineRunner commandLineRunner(TrainingRepository trainingRepository, StudentRepository studentRepository){
		return args -> {
			// SAVING TRAININGS
			Training t1 = trainingRepository.save(new Training("Java and Spring Boot", new Date(), true));
			Training t2 = trainingRepository.save(new Training("Python and Big Data", new Date(), true));
			Training t3 = trainingRepository.save(new Training("Cryptography and Security", new Date(), false));
			Training t4 = trainingRepository.save(new Training("JavaScript and React js", new Date(), true));
			Training t5 = trainingRepository.save(new Training("TypeScript and Angular", new Date(), false));
			trainingRepository.findAll().forEach(training -> {System.out.println(training.toString());});

			// SAVING STUDENTS
			studentRepository.save(new Student("Vieux", "Soumare", "vieuxsoumare@saraya.edu.sn", "+221778541204", new Date(), "", "", 'M', true, t2));
			studentRepository.save(new Student("Bamba", "Ndour", "ndourbamba07@saraya.edu.sn", "+221774512045", new Date(), "", "", 'M', true, t1));
			studentRepository.save(new Student("Maimouna", "Diagne", "diagnemaimouna@saraya.edu.sn", "+221774512047", new Date(), "", "", 'F', true, t1));
			studentRepository.save(new Student("Coumba", "Ndoye", "ndoyecoumba@saraya.edu.sn", "+221779546336", new Date(), "", "", 'F', false, t4));
			studentRepository.save(new Student("Sokhna Boussa", "Gaye", "gayesokhna04@saraya.edu.sn", "+221786368504", new Date(), "", "", 'F', true, t5));
			studentRepository.save(new Student("Nabou", "Ndiaye", "ndiayenabou10@saraya.edu.sn", "+221789568304", new Date(), "", "", 'F', true, t5));
			studentRepository.save(new Student("Salla Diop", "Dia", "diasala02@saraya.edu.sn", "+221774571203", new Date(), "", "", 'F', false, t3));
			studentRepository.save(new Student("Coumba", "Diallo", "diallocoumba@saraya.edu.sn", "+221778950433", new Date(), "", "", 'F', true, t4));
			studentRepository.save(new Student("Yassine", "Diop", "diopyassine@saraya.edu.sn", "+221785497293", new Date(), "", "", 'F', true, t1));
			studentRepository.save(new Student("Sokhna", "Ndiaye", "ndiayesokhna01@saraya.edu.sn", "+221773850600", new Date(), "", "", 'F', false, t3));
			studentRepository.findAll().forEach(student -> {System.out.println(student.getFirstName()+" "+student.getLastName());});
		};
	}

}
