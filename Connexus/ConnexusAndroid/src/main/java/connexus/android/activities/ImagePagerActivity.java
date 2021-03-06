package connexus.android.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import connexus.android.Config;
import connexus.android.R;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * Additions by Preston Landers
 */
public class ImagePagerActivity extends BaseActivity {
    private static final String TAG = "ImagePagerActivity";

    private static final String STATE_POSITION = "STATE_POSITION";

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    ViewPager pager;
    PhotoViewAttacher mAttacher;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_image_pager);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String[] imageUrls = bundle.getStringArray(Config.IMAGES);
        String[] imageLabels = bundle.getStringArray(Config.IMAGE_LABELS);
        String streamName = bundle.getString(Config.STREAM_NAME);
        int pagerPosition = bundle.getInt(Config.IMAGE_POSITION, 0);

        setTitle(streamName);

        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ImagePagerAdapter(imageUrls, imageLabels));
        pager.setCurrentItem(pagerPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, pager.getCurrentItem());
    }


    private class ImagePagerAdapter extends PagerAdapter {

        private String[] imageUrls;
        private String[] imageLabels;
        private LayoutInflater inflater;

        ImagePagerAdapter(String[] imageUrls, String[] imageLabels) {
            this.imageUrls = imageUrls;
            this.imageLabels = imageLabels;
            inflater = getLayoutInflater();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public void finishUpdate(View container) {
        }

        @Override
        public int getCount() {
            return imageUrls.length;
        }

        @Override
        public Object instantiateItem(final ViewGroup viewGroup, int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image, viewGroup, false);
            final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.pager_image);
            mAttacher = new PhotoViewAttacher(imageView);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.pager_loading);
            TextView textView = (TextView) imageLayout.findViewById(R.id.pagerimage_text);
            textView.setText(imageLabels[position] == null ? " " : imageLabels[position]);
            imageLoader.displayImage(imageUrls[position], imageView, getDisplayOptions(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    String message = null;
                    switch (failReason.getType()) {
                        case IO_ERROR:
                            message = "Input/Output error";
                            break;
                        case DECODING_ERROR:
                            message = "Image can't be decoded";
                            break;
                        case NETWORK_DENIED:
                            message = "Downloads are denied";
                            break;
                        case OUT_OF_MEMORY:
                            message = "Out Of Memory error";
                            break;
                        case UNKNOWN:
                            message = "Unknown error";
                            break;
                    }
                    Toast.makeText(ImagePagerActivity.this, message, Toast.LENGTH_SHORT).show();

                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);

                    // We want the following behavior:
                    //  - show complete image initially (fill one axis)
                    //  - allow zooming that takes up the entire viewport
                    // In order to do this, I had to make the initial ImageView layout fit the content initial
                    // size, but then after the zoom is attached and image loaded, we change the ImageView to
                    // fill its parent container.

                    ViewGroup.LayoutParams layoutParams = viewGroup.getLayoutParams();
                    layoutParams.width = layoutParams.FILL_PARENT;
                    layoutParams.height = layoutParams.FILL_PARENT;
                    imageView.setLayoutParams(layoutParams);
                }
            });

            ((ViewPager) viewGroup).addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View container) {
        }
    }
}