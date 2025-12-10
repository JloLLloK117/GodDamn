package Window.Studing;

import java.util.List;

public class ExerciseLoader {

    public static List<TestQuestion> load(String language, int exerciseId) {
        if ("java".equalsIgnoreCase(language)) {
            if (!JavaQuestions.hasExercise(exerciseId)) {
                System.err.println("Упражнение " + exerciseId + " не найдено для Java");
                return List.of();
            }
            return JavaQuestions.getExercise(exerciseId);
        } else if ("cpp".equalsIgnoreCase(language)) {
            if (!CppQuestions.hasExercise(exerciseId)) {
                System.err.println("Упражнение " + exerciseId + " не найдено для C++");
                return List.of();
            }
            return CppQuestions.getExercise(exerciseId);
        } else {
            System.err.println("Неизвестный язык: " + language);
            return List.of();
        }
    }
}

