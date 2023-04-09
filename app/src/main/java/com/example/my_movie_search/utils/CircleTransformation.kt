package com.example.my_movie_search.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import com.squareup.picasso.Transformation


class CircleTransformation : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        // Определяем шаблон обрезки...
        val path = Path()

        // ...как круг
        path.addCircle(
            (source.width / 2).toFloat(),
            (source.height / 2).toFloat(),
            (source.width / 2).toFloat(),
            Path.Direction.CCW
        )

        // Создаём битмап, который и будет результирующим
        val answerBitMap = Bitmap.createBitmap(
            source.width,
            source.height,
            Bitmap.Config.ARGB_8888
        )

        // Создаём холст для нового битмапа
        val canvas = Canvas(answerBitMap)

        // Обрезаем холст по кругу (по шаблону)
        canvas.clipPath(path)

        // А теперь рисуем на этом холсте исходное изображение
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.drawBitmap(source, 0f, 0f, paint)
        source.recycle()

        return answerBitMap
    }

    override fun key(): String {
        return "circle"
    }
}