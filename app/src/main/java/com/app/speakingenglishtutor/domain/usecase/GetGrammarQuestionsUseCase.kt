package com.app.speakingenglishtutor.domain.usecase

import com.app.speakingenglishtutor.data.model.Difficulty
import com.app.speakingenglishtutor.data.model.GrammarQuestion
import com.app.speakingenglishtutor.data.repository.GrammarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGrammarQuestionsUseCase @Inject constructor(
    private val grammarRepository: GrammarRepository
) {
    suspend operator fun invoke(difficulty: Difficulty): Flow<Result<List<GrammarQuestion>>> {
        return grammarRepository.getGrammarQuestions(difficulty)
    }
}
