package com.tanya.recipecompose.domain.util

/**
 * Handles mapping of data from
 * network to model and model to network
 */
interface DomainMapper<T, DomainModel> {

    fun mapToDomainModel(model: T): DomainModel

    fun mapFromDomainModel(domainModel: DomainModel): T

}