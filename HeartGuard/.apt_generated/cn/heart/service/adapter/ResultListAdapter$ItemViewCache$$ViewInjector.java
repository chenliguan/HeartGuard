// Generated code from Butter Knife. Do not modify!
package cn.heart.service.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class ResultListAdapter$ItemViewCache$$ViewInjector<T extends cn.heart.service.adapter.ResultListAdapter.ItemViewCache> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034279, "field 'mImageHead'");
    target.mImageHead = finder.castView(view, 2131034279, "field 'mImageHead'");
    view = finder.findRequiredView(source, 2131034282, "field 'mWholeTime'");
    target.mWholeTime = finder.castView(view, 2131034282, "field 'mWholeTime'");
    view = finder.findRequiredView(source, 2131034280, "field 'mNick'");
    target.mNick = finder.castView(view, 2131034280, "field 'mNick'");
    view = finder.findRequiredView(source, 2131034284, "field 'mRateAverage'");
    target.mRateAverage = finder.castView(view, 2131034284, "field 'mRateAverage'");
    view = finder.findRequiredView(source, 2131034287, "field 'mSypsHeart'");
    target.mSypsHeart = finder.castView(view, 2131034287, "field 'mSypsHeart'");
    view = finder.findRequiredView(source, 2131034288, "field 'mImageView'");
    target.mImageView = finder.castView(view, 2131034288, "field 'mImageView'");
    view = finder.findRequiredView(source, 2131034281, "field 'mRateGrade'");
    target.mRateGrade = finder.castView(view, 2131034281, "field 'mRateGrade'");
    view = finder.findRequiredView(source, 2131034286, "field 'mSypsRhythm'");
    target.mSypsRhythm = finder.castView(view, 2131034286, "field 'mSypsRhythm'");
  }

  @Override public void reset(T target) {
    target.mImageHead = null;
    target.mWholeTime = null;
    target.mNick = null;
    target.mRateAverage = null;
    target.mSypsHeart = null;
    target.mImageView = null;
    target.mRateGrade = null;
    target.mSypsRhythm = null;
  }
}
