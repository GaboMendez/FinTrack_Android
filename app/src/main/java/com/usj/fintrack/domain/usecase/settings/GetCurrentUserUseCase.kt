package com.usj.fintrack.domain.usecase.settings

import com.usj.fintrack.domain.model.User
import com.usj.fintrack.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Returns the first persisted user (id = 1) as a pre-auth stand-in for
 * "current user".  Returns null when no user has been created yet.
 *
 * Once Google Sign-In is implemented this can be replaced with a proper
 * `GetCurrentUserUseCase` that reads from [AuthRepository].
 */
class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): User? = userRepository.getUserById(1L)
}
