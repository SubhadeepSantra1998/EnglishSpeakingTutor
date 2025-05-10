package com.app.speakingenglishtutor.data.repository

import com.app.speakingenglishtutor.data.model.Difficulty
import com.app.speakingenglishtutor.data.model.GrammarQuestion
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray
import javax.inject.Inject

class GrammarRepositoryImpl @Inject constructor(
    private val generativeModel: GenerativeModel
) : GrammarRepository {

    override suspend fun getGrammarQuestions(difficulty: Difficulty): Flow<Result<List<GrammarQuestion>>> = flow {
        try {
            val prompt = buildGeminiPrompt(difficulty)
            
            val response = generativeModel.generateContent(
                content {
                    text(prompt)
                }
            )
            val responseText = response.text?.trim() ?: ""
            
            // Extract JSON array from the response
            val jsonStart = responseText.indexOf('[')
            val jsonEnd = responseText.lastIndexOf(']') + 1
            
            if (jsonStart in 0..<jsonEnd) {
                val jsonString = responseText.substring(jsonStart, jsonEnd)
                val questions = parseGrammarQuestions(jsonString)
                emit(Result.success(questions))
            } else {
                emit(Result.failure(Exception("Invalid response format from Gemini API")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    private fun parseGrammarQuestions(jsonString: String): List<GrammarQuestion> {
        val questions = mutableListOf<GrammarQuestion>()
        try {
            val jsonArray = JSONArray(jsonString)
            
            for (i in 0 until jsonArray.length()) {
                val questionObj = jsonArray.getJSONObject(i)
                
                val id = questionObj.getInt("id")
                val sentence = questionObj.getString("sentence")
                val answer = questionObj.getString("answer")
                
                val optionsObj = questionObj.getJSONObject("options")
                val options = mapOf(
                    "A" to optionsObj.getString("A"),
                    "B" to optionsObj.getString("B"),
                    "C" to optionsObj.getString("C"),
                    "D" to optionsObj.getString("D")
                )
                
                val feedbackObj = questionObj.getJSONObject("feedback")
                val feedback = mapOf(
                    "A" to feedbackObj.getString("A"),
                    "B" to feedbackObj.getString("B"),
                    "C" to feedbackObj.getString("C"),
                    "D" to feedbackObj.getString("D")
                )
                
                questions.add(
                    GrammarQuestion(
                        id = id,
                        sentence = sentence,
                        options = options,
                        answer = answer,
                        feedback = feedback
                    )
                )
            }
        } catch (e: Exception) {
            throw Exception("Error parsing grammar questions: ${e.message}")
        }
        
        return questions
    }
    
    private fun buildGeminiPrompt(difficulty: Difficulty): String {
        return """
            Generate 10 English grammar practice questions in JSON format at the '${difficulty.label}' difficulty level.
     
            Difficulty levels:
            - "easy": short sentences with 1 blank; basic grammar (tenses, articles, simple prepositions)
            - "medium": longer or compound sentences with up to 2 blanks; intermediate grammar (conjunctions, irregular verbs, time expressions)
            - "hard": long, academic or complex sentences with 2 or more blanks; advanced grammar (conditionals, modals, passive voice, phrasal verbs)
     
            Each question should include:
            - Sentence with one or more blanks (___)
            - 4 multiple-choice options (A–D)
            - Correct answer (e.g., "C")
            - Feedback for each option
     
            Use this JSON schema:
     
            GrammarQuestion = {
              "id": int,
              "difficulty": str,
              "sentence": str,
              "options": {
                "A": str,
                "B": str,
                "C": str,
                "D": str
              },
              "answer": str,
              "feedback": {
                "A": str,
                "B": str,
                "C": str,
                "D": str
              }
            }
     
            Return: list[GrammarQuestion]
        """.trimIndent()
    }
}
