package com.project.English365.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.English365.entities.WordDefinition;

@Repository("wordDefinitionsRepository")
public interface WordDefinitionsRepository extends JpaRepository<WordDefinition, Long> {

	void deleteById(Long id);
}