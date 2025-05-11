package com.backend.formulario.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.backend.formulario.entities.CepInfo;

@Repository
public interface CepInfoRepository extends CrudRepository<CepInfo, Long> {

}
