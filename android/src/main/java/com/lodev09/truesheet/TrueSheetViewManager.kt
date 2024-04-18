package com.lodev09.truesheet

import android.util.Log
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableType
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.lodev09.truesheet.core.DismissEvent
import com.lodev09.truesheet.core.PresentEvent
import com.lodev09.truesheet.core.SizeChangeEvent
import com.lodev09.truesheet.core.Utils

class TrueSheetViewManager : ViewGroupManager<TrueSheetView>() {
  override fun getName() = TAG

  override fun createViewInstance(reactContext: ThemedReactContext): TrueSheetView = TrueSheetView(reactContext)

  override fun onDropViewInstance(view: TrueSheetView) {
    super.onDropViewInstance(view)
    view.onHostDestroy()
  }

  override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any>? =
    MapBuilder.builder<String, Any>()
      .put(PresentEvent.EVENT_NAME, MapBuilder.of("registrationName", "onPresent"))
      .put(DismissEvent.EVENT_NAME, MapBuilder.of("registrationName", "onDismiss"))
      .put(SizeChangeEvent.EVENT_NAME, MapBuilder.of("registrationName", "onSizeChange"))
      .build()

  @ReactProp(name = "maxHeight")
  fun setMaxHeight(view: TrueSheetView, height: Double) {
    view.setMaxHeight(Utils.toPixel(height))
  }

  @ReactProp(name = "dismissible")
  fun setDismissible(view: TrueSheetView, dismissible: Boolean) {
    view.setDismissible(dismissible)
  }

  @ReactProp(name = "sizes")
  fun setSizes(view: TrueSheetView, sizes: ReadableArray) {
    val result = ArrayList<Any>()
    for (i in 0 until minOf(sizes.size(), 3)) {
      when (sizes.getType(i)) {
        ReadableType.Number -> result.add(sizes.getDouble(i))
        ReadableType.String -> result.add(sizes.getString(i))
        else -> Log.d(TAG, "Invalid type")
      }
    }

    view.setSizes(result.toArray())
  }

  override fun addEventEmitters(reactContext: ThemedReactContext, view: TrueSheetView) {
    super.addEventEmitters(reactContext, view)
    view.rootSheetView.eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, view.getId())
  }

  companion object {
    const val TAG = "TrueSheetView"
  }
}
