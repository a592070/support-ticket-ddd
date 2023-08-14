package com.example.supportticketddd.repository

import com.example.supportticketddd.entity.AggregateRoot

interface AbstractRepository<Entity extends AggregateRoot, ID> {

    List<Entity> findAll()
    Optional<Entity> findById(ID id)
    ID save(Entity entity)
    void deleteById(ID id)
}