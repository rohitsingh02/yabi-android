package com.yabi.yabiuserandroid.ui.uiutils;

/**
 * Created by yogeshmadaan on 11/11/15.
 */
//public class DrawerUtil {
//    public static Drawer drawer;
//    public static Drawer setUpDrawer(final AppCompatActivity activity, Toolbar toolbar, int selectedItem)
//    {
//        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIcon(R.drawable.ic_home_black_24dp).withName("Home").withTintSelectedIcon(true).withIconTinted(false).withIconTintingEnabled(true);
//        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIcon(R.drawable.ic_face_black_24dp).withName("Profile").withTintSelectedIcon(true).withIconTinted(false).withIconTintingEnabled(true);
//        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIcon(R.drawable.ic_share_24dp).withName("Share with friends").withTintSelectedIcon(true).withIconTinted(false).withIconTintingEnabled(true);
////        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIcon(R.drawable.ic_credit_card_black_24dp).withName("Wallet").withTintSelectedIcon(true).withIconTinted(false).withIconTintingEnabled(true);
//        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIcon(R.drawable.ic_rate_us_24d).withName("Rate Us").withTintSelectedIcon(true).withIconTinted(false).withIconTintingEnabled(true);
////        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIcon(R.drawable.ic_error_outline_black_24dp).withName("Faq").withTintSelectedIcon(true).withIconTinted(false).withIconTintingEnabled(true);
//
//        List<PrimaryDrawerItem> items = new ArrayList<>();
//
//        AccountHeader headerResult = new AccountHeaderBuilder()
//                .withActivity(activity)
//                .withHeaderBackground(R.drawable.drawer)
//                .withSelectionListEnabledForSingleProfile(false)
//                .withOnlyMainProfileImageVisible(true)
//                .withCurrentProfileHiddenInList(true)
//                .withProfileImagesVisible(false)
//                .withCompactStyle(false)
//                .addProfiles(
//                        new ProfileDrawerItem().withName(new SharedPrefUtils(activity).getUserName()).withEmail(new SharedPrefUtils(activity).getUserEmail())
//                )
//                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
//                    @Override
//                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
//                        return false;
//                    }
//                })
//                .build();
//
//       drawer = new DrawerBuilder()
//               .addDrawerItems(item1,item2,item3,item4)
//                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    @Override
//                    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
//                        switch(i)
//                        {
//
//                            case 1:
//                                Intent intent = new Intent(activity, HomeActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                activity.startActivity(intent);
//                                activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
//                                return false;
//                            case 2:
//                                activity.startActivity(new Intent(activity, ProfileActivity.class));
//                                activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
//                                return false;
//                            case 3:
//                                Intent myIntent = new Intent();
//                                myIntent.setAction(Intent.ACTION_SEND);
//                                myIntent.setType("text/plain");
//                                myIntent.putExtra(Intent.EXTRA_TEXT, AppConstants.playstoreLink);
//                                myIntent.putExtra(Intent.EXTRA_SUBJECT, AppConstants.shareText);
//                                activity.startActivity(Intent.createChooser(myIntent, "Invite via"));
//                                activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
//                                return false;
//                            case 4:
//                                new Utils().launchMarket(activity);
//                                activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
//                                return false;
////                            case 4:
////                                activity.startActivity(new Intent(activity, GalleryActivity.class));
////                                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
////                                return false;
////                            case 6:
////                                Intent intent = new Intent(activity, OutletsListActivity.class);
////                                Bundle bundle = new Bundle();
////                                bundle.putString("choiceMode", OutletsAdapter.OutletChoiceMode.BrowseOutlet.toString());
////                                intent.putExtras(bundle);
////                                activity.startActivity(intent);
////                                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
////                                return false;
//                        }
//                        return false;
//                    }
//                })
//                .withActivity(activity)
//                .withActionBarDrawerToggle(true)
//                .withToolbar(toolbar)
//                .withFullscreen(true)
//                .withSelectedItem(selectedItem)
//                .withAccountHeader(headerResult)
//                .withHeaderDivider(true)
//                .build();
//        for (PrimaryDrawerItem item : items)
//        {
//            drawer.addItem(item);
//        }
////        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        return drawer;
//    }
//    private static boolean MyStartActivity(Intent aIntent,AppCompatActivity activity) {
//        try
//        {
//            activity.startActivity(aIntent);
//            return true;
//        }
//        catch (ActivityNotFoundException e)
//        {
//            return false;
//        }
//    }
//
//
//}
