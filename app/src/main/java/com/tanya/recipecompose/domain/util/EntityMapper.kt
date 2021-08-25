package com.tanya.recipecompose.domain.util

/**
 * Handles mapping of data from
 * network to model and model to network
 */
interface EntityMapper<Entity, DomainModel> {

    fun mapFromEntity(entity: Entity): DomainModel

    fun mapToEntity(domainModel: DomainModel): Entity

}