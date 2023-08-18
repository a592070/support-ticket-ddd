package com.example.supportticketddd.common

import com.example.supportticketddd.common.AggregateRoot

interface AbstractRepository<Entity extends AggregateRoot, ID> {

    List<Entity> findAll()
    Optional<Entity> findById(ID id)
    ID save(Entity entity)
    void deleteById(ID id)
}