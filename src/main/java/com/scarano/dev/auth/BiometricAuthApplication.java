package com.scarano.dev.auth;

import com.scarano.dev.auth.model.dtos.UsuarioDTO;
import com.scarano.dev.auth.model.entidade.Cargo;
import com.scarano.dev.auth.model.entidade.Nivel;
import com.scarano.dev.auth.repository.CargoRepositorio;
import com.scarano.dev.auth.repository.ImpDigitalRepositorio;
import com.scarano.dev.auth.repository.NivelRepositorio;
import com.scarano.dev.auth.service.UsuarioServico;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;

@EnableAutoConfiguration
@SpringBootApplication
public class BiometricAuthApplication {
	private static final Logger log = LoggerFactory.getLogger(BiometricAuthApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(BiometricAuthApplication.class, args);
	}
	private static final int MAX_REQUEST_SIZE_IN_MB = 150;

	public CommandLineRunner demo(
			UsuarioServico usuarioServico,
			CargoRepositorio cargoRepositorio,
			ImpDigitalRepositorio impDigitalRepositorio,
			NivelRepositorio nivelRepositorio
	) {
		return (args) -> {
			log.info("-------------------------------");

			log.info("Criando Nivel Administrador");
			Nivel admin = new Nivel();
			admin.setDescription("Administrador");
			nivelRepositorio.save(admin);

			log.info("Criando Cargo Diretor");
			Cargo diretor = new Cargo();
			diretor.setDescription("Diretor");
			diretor.setNivel(admin);
			cargoRepositorio.save(diretor);

			log.info("Criando Usuario com cargo Diretor");
			UsuarioDTO user = new UsuarioDTO(
				0,
				"Lucca",
				"Scarano",
				diretor.getId(),
				"scarano",
				"teste123");
			usuarioServico.save(user);

			log.info("Ready to go!");
			log.info("-------------------------------");
		};
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		final MultipartConfigFactory factory = new MultipartConfigFactory();

		factory.setMaxFileSize(DataSize.ofMegabytes(MAX_REQUEST_SIZE_IN_MB));
		factory.setMaxRequestSize(DataSize.ofMegabytes(MAX_REQUEST_SIZE_IN_MB));

		return factory.createMultipartConfig();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}
		};
	}
}
