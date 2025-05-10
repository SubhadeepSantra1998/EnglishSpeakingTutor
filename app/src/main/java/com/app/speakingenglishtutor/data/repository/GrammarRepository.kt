package com.app.speakingenglishtutor.data.repository

import com.app.speakingenglishtutor.data.model.Difficulty
import com.app.speakingenglishtutor.data.model.GrammarQuestion
import kotlinx.coroutines.flow.Flow

interface GrammarRepository {
    suspend fun getGrammarQuestions(difficulty: Difficulty): Flow<Result<List<GrammarQuestion>>>
}
