package com.dsoft.data.mapper

import com.dsoft.data.model.OrganizationDTO
import com.dsoft.data.model.OrganizationDetailsDTO
import com.dsoft.domain.model.Organization
import com.dsoft.domain.model.OrganizationDetails
import com.dsoft.domain.util.secondsToLocalTime
import com.dsoft.domain.util.timestampToLocalDateTime
import javax.inject.Inject

typealias Mapper<E, T> = (E) -> T


val organizationModelMapper: Mapper<OrganizationDTO, List<Organization>> = { response ->
    response.data?.map { payload ->
        Organization(
            id = payload.id ?: 0,
            name = payload.name.orEmpty(),
            photo = payload.photo.orEmpty(),
            rate = payload.rate ?: 0.0,
            cuisines = payload.cuisines.orEmpty(),
            isFavorite = payload.isFavorite ?: false,
            distance = payload.distance ?: 0F
        )
    }.orEmpty()
}

val organizationDetailsMapper: Mapper<OrganizationDetailsDTO, OrganizationDetails> = { response ->
    OrganizationDetails(
        id = response.id ?: 0,
        name = response.name.orEmpty(),
        email = response.email.orEmpty(),
        categoryName = response.categoryName.orEmpty(),
        detailedInfo = response.detailedInfo.orEmpty(),
        photos = response.photos.orEmpty(),
        phones = response.phones.orEmpty(),
        urls = response.urls.orEmpty(),
        socials = response.socials?.map(socialsMapper).orEmpty(),
        location = response.location.let(locationMapper),
        schedule = response.schedule?.map(scheduleMapper).orEmpty(),
        rateCount = response.rateCount ?: 0,
        rate = response.rate ?: 0.0,
        averageCheck = response.averageCheck.orEmpty(),
        services = response.services.orEmpty(),
        serviceLanguages = response.serviceLanguages.orEmpty(),
        cuisines = response.cuisines.orEmpty(),
        reviewCount = response.reviewCount ?: 0,
        review = response.review.let(reviewMapper),
        distance = response.distance ?: 0F,
        discount = response.discount ?: 0,
        isFavorite = response.isFavorite ?: false
    )
}

private val socialsMapper: Mapper<OrganizationDetailsDTO.SocialPayload?, OrganizationDetails.Social> = { response ->
    OrganizationDetails.Social(
        id = response?.id ?: 0,
        type = response?.type ?: 0,
        name = response?.name.orEmpty(),
        url = response?.url.orEmpty(),
        organization = response?.organization ?: 0
    )
}

private val locationMapper: Mapper<OrganizationDetailsDTO.LocationPayload?, OrganizationDetails.Location> = { response ->
    OrganizationDetails.Location(
        id = response?.id ?: 0,
        latitude = response?.latitude ?: 0F,
        longitude = response?.longitude ?: 0F,
        city = response?.city.orEmpty(),
        address = response?.address.orEmpty(),
        organization = response?.organization ?: 0,
        district = response?.district ?: 0
    )
}

private val scheduleMapper: Mapper<OrganizationDetailsDTO.SchedulePayload?, OrganizationDetails.Schedule> = { response ->
    OrganizationDetails.Schedule(
        day = response?.day ?: 0,
        start = secondsToLocalTime(response?.start ?: 0) ,
        end = secondsToLocalTime(response?.end ?: 0)
    )
}

private val reviewMapper: Mapper<OrganizationDetailsDTO.ReviewPayload?, OrganizationDetails.Review> = { response ->
    OrganizationDetails.Review(
        organization = response?.organization.orEmpty(),
        user = response?.user.let(userMapper),
        rate = response?.rate ?: 0,
        text = response?.text.orEmpty(),
        publicationDate = timestampToLocalDateTime(response?.publicationDate ?: 0F)
    )
}

private val userMapper: Mapper<OrganizationDetailsDTO.UserPayload?, OrganizationDetails.User> = { response ->
    OrganizationDetails.User(
        email = response?.email.orEmpty(),
        profile = response?.profile.let(profileMapper)
    )
}

private val profileMapper: Mapper<OrganizationDetailsDTO.ProfilePayload?, OrganizationDetails.Profile> = { response ->
    OrganizationDetails.Profile(
        id = response?.id ?: 0,
        email = response?.email.orEmpty(),
        firstName = response?.firstName.orEmpty(),
        lastName = response?.lastName.orEmpty(),
        avatar = response?.avatar.orEmpty(),
        organizations = response?.organizations.orEmpty()
    )
}