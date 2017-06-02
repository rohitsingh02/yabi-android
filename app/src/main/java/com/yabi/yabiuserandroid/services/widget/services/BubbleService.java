package com.yabi.yabiuserandroid.services.widget.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.yabi.yabiuserandroid.R;
import com.yabi.yabiuserandroid.SupportedMerchants;
import com.yabi.yabiuserandroid.adapters.OfferAdapter;
import com.yabi.yabiuserandroid.chatmessageview.models.Message;
import com.yabi.yabiuserandroid.chatmessageview.models.User;
import com.yabi.yabiuserandroid.chatmessageview.utils.ChatBot;
import com.yabi.yabiuserandroid.chatmessageview.views.ChatView;
import com.yabi.yabiuserandroid.models.data.MerchantDetail;
import com.yabi.yabiuserandroid.models.data.Offers;
import com.yabi.yabiuserandroid.network.ErrorTypes;
import com.yabi.yabiuserandroid.network.NetworkManager;
import com.yabi.yabiuserandroid.utils.SharedPrefUtils;
import com.yabi.yabiuserandroid.utils.Utils;

import java.util.List;
import java.util.Random;

import de.greenrobot.event.EventBus;

import static com.yabi.yabiuserandroid.R.drawable.yabi_splash1;

/**
 * Created by rohitsingh on 26/12/16.
 */

public class BubbleService extends Service {

    ChatView chatView;
    BubbleLayout bubbleView;

    public static int sSpringTension = 200;
    public static int sSpringFriction = 20;
    private static Spring sBubbleSpring;
    private static Spring sContentSpring;
    private RelativeLayout offerContainer, offerDetailContainer, header, bubbleContainer;
    private BubblesManager bubblesManager;
    private ImageView  profileImg;
    String currentPackageName;
    private ProgressBar progressBar;
    String merchantName = null;
    int merchantId = -1;
    String packageName = null;
    private WindowManager windowManager;
    private View mContent;
    private boolean mbExpanded = false;
    EventBus eventBus = null;
    private int[] mPos = {0, -20};
    int offerCount;
    private User user;
    private View topArrawView;

    private TextView txtOfferCount, txtOfferDetail, txtHeader;
    private ImageView chatheadImg;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Utils.LogTag, "ChatHeadService.onBind()");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeBubbleManager();
        eventBus = EventBus.getDefault();
        eventBus.register(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null && intent.getExtras() != null && intent.getExtras().getString("package_name") != null) {
            packageName = intent.getExtras().getString("package_name");
            if (new SupportedMerchants().isMerchant(packageName,getBaseContext()) != null) {
                packageName = new SupportedMerchants().isMerchant(packageName,getBaseContext()).getAndroid_app();
                merchantName = new SupportedMerchants().isMerchant(packageName,getBaseContext()).getAndroid_app();
                merchantId = new SupportedMerchants().isMerchant(packageName,getBaseContext()).getId();
                if (currentPackageName != packageName) {

                    NetworkManager.getInstance(BubbleService.this).getMerchantDetails("merchant/" + merchantId);

                    currentPackageName = packageName;
                }
            } else {
                stopService(new Intent(BubbleService.this, BubbleService.class));
            }
        } else
            Log.e("intent", "null");




//        return START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        bubblesManager.recycle();
        super.onDestroy();


    }

    /*
    * Inflate notifation layout  into bubble layout
    */
    private void initiateBubble(List<Offers> offersList) {

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        bubbleView = (BubbleLayout) LayoutInflater.from(this)
                .inflate(R.layout.notification_layout, null);

        mContent = (LinearLayout) bubbleView.findViewById(R.id.content);
        mContent.setScaleX(0.0f);
        mContent.setScaleY(0.0f);
        ViewGroup.LayoutParams contentParams = mContent.getLayoutParams();
        contentParams.width = Utils.getScreenWidth(this);
        contentParams.height = Utils.getScreenHeight(this);
        mContent.setLayoutParams(contentParams);

        chatheadImg = (ImageView) bubbleView.findViewById(R.id.chathead_img);
        //listView = (ListView) bubbleView.findViewById(R.id.list_view);
        chatView = (ChatView) bubbleView.findViewById(R.id.chat_view);
        topArrawView = bubbleView.findViewById(R.id.top_arrow);
        progressBar = (ProgressBar) bubbleView.findViewById(R.id.progress);
        txtOfferCount = (TextView) bubbleView.findViewById(R.id.txt_total_offers_count);
        offerContainer = (RelativeLayout) bubbleView.findViewById(R.id.offerContainer);
        txtOfferDetail = (TextView) bubbleView.findViewById(R.id.txt_offer_detail_message);
        offerDetailContainer = (RelativeLayout) bubbleView.findViewById(R.id.layout_offer_details);


        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        params.height = Utils.getScreenHeight(this);
        params.width = Utils.getScreenWidth(this);


        SpringSystem system = SpringSystem.create();
        SpringConfig springConfig = new SpringConfig(sSpringTension, sSpringFriction);

        sContentSpring = system.createSpring();
        sContentSpring.setSpringConfig(springConfig);
        sContentSpring.setCurrentValue(0.0);
        sContentSpring.addListener(new SpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                float clampedValue = (float) SpringUtil.clamp(value, 0.0, 1.0);
                mContent.setScaleX(value);
                mContent.setScaleY(value);
                mContent.setAlpha(clampedValue);
            }

            @Override
            public void onSpringAtRest(Spring spring) {
                mContent.setLayerType(View.LAYER_TYPE_NONE, null);
                if (spring.currentValueIsApproximately(0.0)) {
                    hideContent();
                }
            }

            @Override
            public void onSpringActivate(Spring spring) {
                mContent.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }

            @Override
            public void onSpringEndStateChange(Spring spring) {

            }
        });

        sBubbleSpring = system.createSpring();
        sBubbleSpring.setSpringConfig(springConfig);
        sBubbleSpring.setCurrentValue(1.0);

        setupList(offersList);


        // this method call when user remove notification layout
        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) {
                Toast.makeText(getApplicationContext(), "Bubble removed !",
                        Toast.LENGTH_SHORT).show();
            }
        });
        // this methoid call when cuser click on the notification layout( bubble layout)
        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {

            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                Toast.makeText(getApplicationContext(), "Clicked !",
                        Toast.LENGTH_SHORT).show();

                WindowManager.LayoutParams params = (WindowManager.LayoutParams) bubbleView.getLayoutParams();

                if (!mbExpanded) {
                    bubbleView.getLocationOnScreen(mPos);
                    mPos[1] -= Utils.getStatusBarHeight(BubbleService.this);

                    topArrawView.setVisibility(View.VISIBLE);
//                    params.flags &= ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//                    params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    sBubbleSpring.setEndValue(0.0);
                    sContentSpring.setEndValue(1.0);
                    showContent();

                } else {
                    topArrawView.setVisibility(View.INVISIBLE);
                    sBubbleSpring.setEndValue(1.0);
                    sContentSpring.setEndValue(0.0);
                    hideContent();
                }
                mbExpanded = !mbExpanded;
                try {
                    windowManager.updateViewLayout(bubbleView, params);

                } catch (Exception e) {
                }

            }
        });

        // add bubble view into bubble manager
        bubblesManager.addBubble(bubbleView, 60, 20);
    }


    private void hideContent() {
        mContent.setVisibility(View.GONE);
        ViewGroup.LayoutParams contentParams = bubbleView.getLayoutParams();
        contentParams.width = contentParams.WRAP_CONTENT;
        contentParams.height = contentParams.WRAP_CONTENT;
        bubbleView.setLayoutParams(contentParams);
        windowManager.updateViewLayout(bubbleView, contentParams);
    }

    private void showContent() {
        ViewGroup.LayoutParams contentParams = bubbleView.getLayoutParams();
        contentParams.width = Utils.getScreenWidth(this) ;
        contentParams.height = Utils.getScreenHeight(this);
        bubbleView.setLayoutParams(contentParams);
        mContent.setVisibility(View.VISIBLE);

    }


    /**
     * Configure the trash layout with your BubblesManager builder.
     */
    private void initializeBubbleManager() {
        bubblesManager = new BubblesManager.Builder(this)
                .setTrashLayout(R.layout.notification_trash_layout)
                .build();
        bubblesManager.initialize();
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showOfferCount() {
        offerContainer.setVisibility(View.VISIBLE);
    }

    private void hideOfferCount() {
        offerContainer.setVisibility(View.GONE);
    }

    private void showOfferDetail() {
        offerDetailContainer.setVisibility(View.VISIBLE);
    }

    private void hideOfferDetail() {
        offerDetailContainer.setVisibility(View.GONE);
    }


    public void onEvent(JsonObject jsonObject) {
        if(jsonObject == null){
            Log.d(Utils.LogTag,"Unable to fetch merchant detail.");
            return;
        } else{
            JsonObject myObj =  jsonObject.getAsJsonObject("data");
            System.out.print(jsonObject);
            MerchantDetail merchantDetail = new Gson().fromJson(myObj,new TypeToken<MerchantDetail>(){}.getType());
            System.out.print(merchantDetail.getOffersList());
            List<Offers> offersList = merchantDetail.getOffersList();
            if(merchantDetail != null && offersList != null && offersList.size() > 0){
                if (bubbleView == null)
                    initiateBubble(offersList);
                else
                    setupList(offersList);
            }
        }
    }




    public void onEvent(ErrorTypes errorTypes)
    {
        if(errorTypes.getType() == ErrorTypes.FETCH_MERCHANT_ERROR)
        {
            Log.d(Utils.LogTag,"Unable to fetch merchant detail.");
        }
    }


    public void setupList(List<Offers> offers) {
        try {
            showProgress();
            hideOfferCount();
            hideOfferDetail();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideProgress();
                    showOfferDetail();
                    handleOfferVisibility();
                }
            }, 2000);
        } catch (Exception e) {

        }
        OfferAdapter offerAdapter;
        offerAdapter = new OfferAdapter(getApplicationContext(), offers);
//        listView.setAdapter(offerAdapter);


        txtOfferCount.setText(String.valueOf(offers.size()));
        if (merchantName != null)
            txtOfferDetail.setText(String.valueOf(offers.size()) + " offers from " + merchantName);
        else
            hideOfferDetail();
        offerCount = offers.size();
        setUpHeader();
        addHeaderContent();
        setChatViewAttributes();
        createChatMessage(offers);
    }




    public void setChatViewAttributes(){


        initUsers();
        //Set UI parameters if you need
        chatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        chatView.setLeftBubbleColor(ContextCompat.getColor(this, R.color.light_gray));
        chatView.setBackgroundColor(Color.WHITE);
        chatView.setSendButtonColor(ContextCompat.getColor(this, R.color.cyan500));
        chatView.setSendIcon(R.drawable.ic_action_send);
        chatView.setRightMessageTextColor(Color.BLACK);
        chatView.setLeftMessageTextColor(Color.BLACK);
        chatView.setUsernameTextColor(Color.BLACK);
        chatView.setSendTimeTextColor(Color.BLACK);
//        chatView.setDateSeparatorColor(Color.BLACK);
        chatView.setInputTextHint("new message...");
//        chatView.setAutoScroll(true);
        chatView.setMessageMarginTop(5);
        chatView.setMessageMarginBottom(5);

        //Click Send Button
        chatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new message
                Message message = new Message.Builder()
                        .setUser(user)
                        .setRightMessage(true)
                        .setMessageText(chatView.getInputText())
                        .hideIcon(true)
                        .build();
                //Set to chat view
                chatView.send(message);
                //Reset edit text
                chatView.setInputText("");

                //Receive message
                final Message receivedMessage = new Message.Builder()
                        .setUser(user)
                        .setRightMessage(false)
                        .setMessageText(ChatBot.talk(user.getName(), message.getMessageText()))
                        .build();

                // This is a demo bot
                // Return within 3 seconds
                int sendDelay = (new Random().nextInt(4) + 1) * 1000;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        chatView.receive(receivedMessage);
                    }
                }, sendDelay);
            }

        });


    }

    public void createChatMessage(List<Offers> offersList){

        for (int i = 0; i < offersList.size(); i++){
            final Message receivedMessage = new Message.Builder()
                    .setUser(user)
                    .setRightMessage(false)
                    .setUsernameVisibility(false)
                    .setOfferTitle(offersList.get(i).getTitle())
                    .setOfferDescription(offersList.get(i).getDescription())
                    .setOfferExpiry(offersList.get(i).getExpiry_date_and_time())
                    .build();

            // This is a demo bot
            // Return within 3 seconds
            int sendDelay = (new Random().nextInt(4) + 1) * 1000;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatView.receive(receivedMessage);
                }
            }, sendDelay);
        }
    }


    private void initUsers() {
        //User id
        int myId = 0;
        //User icon
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.yabi_splash1);
        //User name
        String myName = "Yabi";
        user = new User(myId, myName, myIcon);
    }



    private void setUpHeader() {
        LayoutInflater inflater = LayoutInflater.from(this);
        header = (RelativeLayout) inflater.inflate(R.layout.widget_offers_header, null, false);
        profileImg = (ImageView) header.findViewById(R.id.img_profile);
        txtHeader = (TextView) header.findViewById(R.id.txt_header_content);
    }

    private void addHeaderContent() {
        SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(this);
//        String url = sharedPrefUtils.getUserProfilePic();
//        if (url != null) {
//            Glide.with(this).load(url).downloadOnly((int) getResources().getDimension(R.dimen.signin_profile_width), (int) getResources().getDimension(R.dimen.signin_profile_width));
//            MyLogger.e("loading", "" + url);
//            Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(profileImg);
//        }
        profileImg.setImageDrawable(getResources().getDrawable(yabi_splash1));
        String headerText = "Hey " + sharedPrefUtils.getUserName() + ", I searched" + String.valueOf(offerCount) + " offers for " + merchantName + ". Here you go:";
        txtHeader.setText(headerText);

    }

    private void handleOfferVisibility() {
        try {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideOfferDetail();
                    showOfferCount();
                }
            }, 2000);
        } catch (Exception e) {

        }
    }

}
