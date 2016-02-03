// Generated code from Butter Knife. Do not modify!
package cn.heart.main.controller;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class ShowResultActivity$$ViewInjector<T extends cn.heart.main.controller.ShowResultActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034175, "field 'mNick'");
    target.mNick = finder.castView(view, 2131034175, "field 'mNick'");
    view = finder.findRequiredView(source, 2131034223, "field 'mImageHeart'");
    target.mImageHeart = finder.castView(view, 2131034223, "field 'mImageHeart'");
    view = finder.findRequiredView(source, 2131034221, "field 'mSymHeartRight'");
    target.mSymHeartRight = finder.castView(view, 2131034221, "field 'mSymHeartRight'");
    view = finder.findRequiredView(source, 2131034201, "field 'mCircleHead'");
    target.mCircleHead = finder.castView(view, 2131034201, "field 'mCircleHead'");
    view = finder.findRequiredView(source, 2131034200, "field 'mRelativeOnclick' and method 'SpeechClickListener'");
    target.mRelativeOnclick = finder.castView(view, 2131034200, "field 'mRelativeOnclick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.SpeechClickListener(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034209, "field 'mSinusArrest'");
    target.mSinusArrest = finder.castView(view, 2131034209, "field 'mSinusArrest'");
    view = finder.findRequiredView(source, 2131034208, "field 'mHeartBeatNum'");
    target.mHeartBeatNum = finder.castView(view, 2131034208, "field 'mHeartBeatNum'");
    view = finder.findRequiredView(source, 2131034224, "field 'mSymHeart'");
    target.mSymHeart = finder.castView(view, 2131034224, "field 'mSymHeart'");
    view = finder.findRequiredView(source, 2131034219, "field 'mSymHeartLeft'");
    target.mSymHeartLeft = finder.castView(view, 2131034219, "field 'mSymHeartLeft'");
    view = finder.findRequiredView(source, 2131034205, "field 'mRateAverage'");
    target.mRateAverage = finder.castView(view, 2131034205, "field 'mRateAverage'");
    view = finder.findRequiredView(source, 2131034216, "field 'mSymRhythm'");
    target.mSymRhythm = finder.castView(view, 2131034216, "field 'mSymRhythm'");
    view = finder.findRequiredView(source, 2131034215, "field 'mImageRhythm'");
    target.mImageRhythm = finder.castView(view, 2131034215, "field 'mImageRhythm'");
    view = finder.findRequiredView(source, 2131034202, "field 'mRateGrade'");
    target.mRateGrade = finder.castView(view, 2131034202, "field 'mRateGrade'");
    view = finder.findRequiredView(source, 2131034207, "field 'mCardiaHeart'");
    target.mCardiaHeart = finder.castView(view, 2131034207, "field 'mCardiaHeart'");
    view = finder.findRequiredView(source, 2131034211, "field 'mPsvcNum'");
    target.mPsvcNum = finder.castView(view, 2131034211, "field 'mPsvcNum'");
    view = finder.findRequiredView(source, 2131034222, "field 'mSymHeartTwo'");
    target.mSymHeartTwo = finder.castView(view, 2131034222, "field 'mSymHeartTwo'");
    view = finder.findRequiredView(source, 2131034203, "field 'mWholeTime'");
    target.mWholeTime = finder.castView(view, 2131034203, "field 'mWholeTime'");
    view = finder.findRequiredView(source, 2131034206, "field 'mRhythmHeart'");
    target.mRhythmHeart = finder.castView(view, 2131034206, "field 'mRhythmHeart'");
    view = finder.findRequiredView(source, 2131034213, "field 'mPvcNum'");
    target.mPvcNum = finder.castView(view, 2131034213, "field 'mPvcNum'");
  }

  @Override public void reset(T target) {
    target.mNick = null;
    target.mImageHeart = null;
    target.mSymHeartRight = null;
    target.mCircleHead = null;
    target.mRelativeOnclick = null;
    target.mSinusArrest = null;
    target.mHeartBeatNum = null;
    target.mSymHeart = null;
    target.mSymHeartLeft = null;
    target.mRateAverage = null;
    target.mSymRhythm = null;
    target.mImageRhythm = null;
    target.mRateGrade = null;
    target.mCardiaHeart = null;
    target.mPsvcNum = null;
    target.mSymHeartTwo = null;
    target.mWholeTime = null;
    target.mRhythmHeart = null;
    target.mPvcNum = null;
  }
}
