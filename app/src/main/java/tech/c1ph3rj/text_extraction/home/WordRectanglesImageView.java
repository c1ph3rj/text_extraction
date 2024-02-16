package tech.c1ph3rj.text_extraction.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;
import com.google.mlkit.vision.text.Text;

import java.util.ArrayList;
import java.util.List;

public class WordRectanglesImageView extends AppCompatImageView {

    private List<Text.Element> words;
    private Paint paint;
    private float scaleFactor;

    public WordRectanglesImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        words = new ArrayList<>();
    }

    public void setWords(List<Text.Element> words, int imageWidth, int imageHeight) {
        this.words.addAll(words);
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        scaleFactor = Math.min((float) viewWidth / imageWidth, (float) viewHeight / imageHeight);
    }

    public void startDraw(){
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (words != null) {
            for (Text.Element word : words) {
                drawWordRectangle(canvas, word);
            }
        }
    }

    private void drawWordRectangle(Canvas canvas, Text.Element word) {
        RectF rect = new RectF(
                word.getBoundingBox().left * scaleFactor,
                word.getBoundingBox().top * scaleFactor,
                word.getBoundingBox().right * scaleFactor,
                word.getBoundingBox().bottom * scaleFactor
        );
        canvas.drawRect(rect, paint);
    }
}
