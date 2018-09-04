import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.drawable.*
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.widget.FrameLayout


class FloatingLayout(context: Context, attrs: AttributeSet?):FrameLayout(context, attrs) {

    private var fabElevation: Float = 0.toFloat()
    private var fabColor: Int = 0

    private var isCreated: Boolean = false

    private var clipPath: Path

    init {
        initTypedArray(attrs)
        clipPath = Path()
    }


    private fun initTypedArray(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.FloatingLayout, 0, 0)


        fabElevation = ta.getDimension(R.styleable.FloatingLayout_flElevation, resources.getDimension(R.dimen.fab_default_elevation))
        fabColor = ta.getColor(R.styleable.FloatingLayout_flColor, ContextCompat.getColor(context, R.color.colorAccent))
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        minimumHeight = resources.getDimensionPixelSize(R.dimen.fab_size_normal)
        minimumWidth = resources.getDimensionPixelSize(R.dimen.fab_size_normal)
         super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun buildView() {
        if (childCount > 1) {
            throw IllegalStateException("Floating Layout must have only one direct child")
        }
        isCreated = true
        initFabBackground()
        initFabShadow()

        requestLayout()
    }

    private fun initFabBackground() {
        val backgroundDrawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.rounded_border_fl)


        backgroundDrawable!!.mutate().setColorFilter(fabColor, PorterDuff.Mode.SRC_IN)

        val selectableDrawable: Drawable
        selectableDrawable = RippleDrawable(ColorStateList.valueOf(Color.argb(150, 255, 255, 255)),
                    null, backgroundDrawable)


        val backgroundLayers = LayerDrawable(arrayOf(backgroundDrawable, selectableDrawable))
        background = backgroundLayers

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!isCreated) {
            buildView()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (!isCreated) {
            buildView()
        }
    }

    private fun initFabShadow() {
        ViewCompat.setElevation(this, fabElevation)
    }


}