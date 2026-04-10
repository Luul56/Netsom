package com.netsom.app.ui.result

import android.view.LayoutInflater
import android.view.ViewGroup
import com.netsom.app.databinding.ResultMiniImageBinding
import com.netsom.app.ui.BaseDiffCallback
import com.netsom.app.ui.NoStateAdapter
import com.netsom.app.ui.ViewHolderState
import com.netsom.app.ui.newSharedPool
import com.netsom.app.ui.settings.Globals.TV
import com.netsom.app.ui.settings.Globals.isLayout
import com.netsom.app.utils.ImageLoader.loadImage

const val IMAGE_CLICK = 0
const val IMAGE_LONG_CLICK = 1

class ImageAdapter(
    val clickCallback: ((Int) -> Unit)? = null,
    val nextFocusUp: Int? = null,
    val nextFocusDown: Int? = null,
) :
    NoStateAdapter<Int>(
        diffCallback = BaseDiffCallback(
            itemSame = Int::equals,
            contentSame = Int::equals
        )
    ) {
    companion object {
        val sharedPool =
            newSharedPool { setMaxRecycledViews(CONTENT, 10) }
    }

    override fun onCreateContent(parent: ViewGroup): ViewHolderState<Any> {
        return ViewHolderState(
            ResultMiniImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onClearView(holder: ViewHolderState<Any>) {
        val binding = holder.view as? ResultMiniImageBinding ?: return
        clearImage(binding.root)
    }

    override fun onBindContent(holder: ViewHolderState<Any>, item: Int, position: Int) {
        val binding = holder.view as? ResultMiniImageBinding ?: return

        binding.root.apply {
            loadImage(item)
            if (nextFocusDown != null) {
                this.nextFocusDownId = nextFocusDown
            }
            if (nextFocusUp != null) {
                this.nextFocusUpId = nextFocusUp
            }
            if (clickCallback != null) {
                if (isLayout(TV)) {
                    isClickable = true
                    isLongClickable = true
                    isFocusable = true
                    isFocusableInTouchMode = true
                }
                setOnClickListener {
                    clickCallback.invoke(IMAGE_CLICK)
                }
                setOnLongClickListener {
                    clickCallback.invoke(IMAGE_LONG_CLICK)
                    return@setOnLongClickListener true
                }
            }
        }
    }
}