package com.example.getsetgoal;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Motionpathadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<MilestoneModel> milestoneModels;
    MilestoneInterface msinterface;
    Activity context;
    boolean isRunning = false;
    boolean ismilestonecompleted;

    public static final String COMPLETED="Completed";
    public static final String NOT_COMPLETED="Not Completed";
    public static final String PARTIALLY_COMPLETED="Partially Completed";

    public Motionpathadapter(Activity context, List<MilestoneModel> milestoneModels, MilestoneInterface msinterface, boolean ismilestonecompleted) {
        this.milestoneModels = milestoneModels;
        this.msinterface = msinterface;
        this.context = context;
        this.ismilestonecompleted = ismilestonecompleted;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_evenitem, parent, false);
            return new ViewHolderEven(view);
        } else if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_odditem, parent, false);
            return new ViewHolderOdd(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_firstitem, parent, false);
            return new ViewHolderFirst(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final MilestoneModel milestone = milestoneModels.get(position);
        if (holder.getItemViewType() == 0) {
            final ViewHolderEven even = (ViewHolderEven) holder;
            even.setData(milestone, position);
        } else if (holder.getItemViewType() == 1) {
            ViewHolderOdd odd = (ViewHolderOdd) holder;
            odd.setData(milestone, position);
        } else {
            ViewHolderFirst first = (ViewHolderFirst) holder;
            first.setData(milestone, position);
        }
    }

    class ViewHolderOdd extends RecyclerView.ViewHolder {

        LottieAnimationView lotti, coin;
        TextView tv_date, tv_text;
        ImageView iv_disable;

        public ViewHolderOdd(@NonNull View itemView) {
            super(itemView);
            lotti = itemView.findViewById(R.id.lotti);
            coin = itemView.findViewById(R.id.coin);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_text = itemView.findViewById(R.id.tv_text);
            iv_disable = itemView.findViewById(R.id.iv_disable);
        }

        public void setData(final MilestoneModel milestone, final int pos) {

            if (pos == milestoneModels.size() - 1) {
                coin.setVisibility(View.VISIBLE);
                if (milestone.getMilestone_iscomplete() == 1 || milestone.isPlayed()) {
                    coin.setAnimation("filled_odd_coin.json");
                    coin.playAnimation();
                } else {
                    coin.setAnimation("dashed_odd_coin.json");
                    coin.playAnimation();
                }
            } else {
                coin.setVisibility(View.GONE);
            }

            tv_text.setText("MS" + milestone.getMilestoneNumber() + ":" + milestone.getMilestoneText());


            String date[]=milestone.getMilestoneStartdate().split("/");
            String strDate=date[0]+"|"+date[1];

            tv_date.setText(strDate);

            iv_disable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isRunning) {
                        if (isPreviousMilestoneComleted(pos)) {

                            if (milestone.getMilestone_iscomplete() == 0) {
                                if (milestone.isPlayed()) {
                                    openUpdateDialog(milestone, getAdapterPosition(), iv_disable,"anim_odd_coin.json");
                                } else {
                                    playAnimation(milestone, pos, "anim_odd.json");
                                }
                            } else {
                                Toast.makeText(context, "milestone already completed", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "please complete previous milestone", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });



            if (milestone.getMilestone_iscomplete() == 1) {
                lotti.setAnimation("filled_odd.json");
                lotti.playAnimation();

                tv_date.setBackgroundResource(R.drawable.bg_rect_purple);
                tv_date.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));

                tv_text.setBackgroundResource(R.drawable.bg_rect_purple);
                tv_text.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));

                switch (milestone.getMilestoneStatus()) {
                    case Motionpathadapter.COMPLETED:
                    case Motionpathadapter.PARTIALLY_COMPLETED:
                        iv_disable.setImageResource(R.drawable.ic_enabled);
                        break;
                    case Motionpathadapter.NOT_COMPLETED:
                        iv_disable.setImageResource(R.drawable.ic_close_enabled);
                        break;
                }


            } else if (milestone.isPlayed()) {
                lotti.setAnimation("filled_odd.json");
                lotti.playAnimation();


                tv_date.setBackgroundResource(R.drawable.bg_rect_purple);
                tv_date.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));

                tv_text.setBackgroundResource(R.drawable.bg_rect_purple);
                tv_text.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));
                iv_disable.setImageResource(R.drawable.ic_unfilled_enabled);
            }
            if (isPreviousMilestoneComleted(pos)) {
                if (milestone.getMilestone_iscomplete() == 0) {
                    if (!milestone.isPlayed()) {
                        playAnimation(milestone, pos, "anim_odd.json");
                    }
                }
            } else {
                lotti.setAnimation("dashed_odd.json");
                lotti.playAnimation();

                tv_date.setBackgroundResource(R.drawable.bg_rect_gray);
                tv_date.setTextColor(context.getResources().getColor(R.color.colorDarkGray));

                tv_text.setBackgroundResource(R.drawable.bg_rect_gray);
                tv_text.setTextColor(context.getResources().getColor(R.color.colorDarkGray));
                iv_disable.setImageResource(R.drawable.ic_disabled);
            }
        }


        public void openUpdateDialog(final MilestoneModel milestoneModel, final int pos, final ImageView iv_disable,final String coinFile) {

            if (milestoneModel.getMilestone_iscomplete() == 0) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                        .setIcon(R.drawable.ic_enabled)
                        .setTitle("Milestone " + milestoneModel.getMilestoneText());

                final View customLayout
                        = context.getLayoutInflater().inflate(R.layout.layout_update_milestone, null);
                alertDialog.setView(customLayout);
                final AlertDialog dialog = alertDialog.create();

                Button btn_complete = customLayout.findViewById(R.id.btn_complete);
                Button btn_notcomplete = customLayout.findViewById(R.id.btn_notcomplete);
                Button btn_partial = customLayout.findViewById(R.id.btn_partial);

                btn_complete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        milestoneModel.setMilestone_iscomplete(1);
                        milestoneModel.setMilestoneStatus(COMPLETED);
                        milestoneModel.setMilestoneTime(System.currentTimeMillis()+"");

//                    notifyItemChanged(pos);
                        iv_disable.setImageResource(R.drawable.ic_enabled);
                        msinterface.onmilestoneUpdate(milestoneModel);

                        notifyItemChanged(pos+1);

                        if (coin != null) {
                            if (coin.getVisibility() == View.VISIBLE) {
                                playCoinAnimation(coin, coinFile);
                            }
                        }
                    }
                });

                btn_notcomplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        milestoneModel.setMilestone_iscomplete(1);
                        milestoneModel.setMilestoneStatus(NOT_COMPLETED);
                        milestoneModel.setMilestoneTime(System.currentTimeMillis()+"");

//                    notifyItemChanged(pos);
                        iv_disable.setImageResource(R.drawable.ic_close_enabled);
                        msinterface.onmilestoneUpdate(milestoneModel);

                        notifyItemChanged(pos+1);
                        if (coin != null) {
                            if (coin.getVisibility() == View.VISIBLE) {
                                playCoinAnimation(coin, coinFile);
                            }
                        }
                    }
                });

                btn_partial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        milestoneModel.setMilestone_iscomplete(1);
                        milestoneModel.setMilestoneStatus(PARTIALLY_COMPLETED);
                        milestoneModel.setMilestoneTime(System.currentTimeMillis()+"");

//                    notifyItemChanged(pos);
                        iv_disable.setImageResource(R.drawable.ic_enabled);
                        msinterface.onmilestoneUpdate(milestoneModel);

                        notifyItemChanged(pos+1);

                        if (coin != null) {
                            if (coin.getVisibility() == View.VISIBLE) {
                                playCoinAnimation(coin, coinFile);
                            }
                        }
                    }
                });
                dialog.show();
            } else {
                Toast.makeText(context, "Milestone is already completed", Toast.LENGTH_SHORT).show();
            }

        }

        private void playAnimation(final MilestoneModel milestoneModel,  final int pos, final String animName) {
            lotti.setAnimation(animName);
            lotti.playAnimation();
            lotti.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    isRunning = true;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    milestoneModel.setPlayed(true);
//                notifyItemChanged(pos);
                    isRunning = false;

                    tv_date.setBackgroundResource(R.drawable.bg_rect_purple);
                    tv_date.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));

                    tv_text.setBackgroundResource(R.drawable.bg_rect_purple);
                    tv_text.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));
                    iv_disable.setImageResource(R.drawable.ic_unfilled_enabled);



                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    isRunning = false;
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
        }
    }

    class ViewHolderFirst extends RecyclerView.ViewHolder {

        LottieAnimationView lotti;
        TextView tv_date, tv_text;
        ImageView iv_disable;

        public ViewHolderFirst(@NonNull View itemView) {
            super(itemView);
            lotti = itemView.findViewById(R.id.lotti);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_text = itemView.findViewById(R.id.tv_text);
            iv_disable = itemView.findViewById(R.id.iv_disable);
        }

        public void setData(final MilestoneModel milestone, final int pos) {

            tv_text.setText("MS" + milestone.getMilestoneNumber() + ":" + milestone.getMilestoneText());

            String date[]=milestone.getMilestoneStartdate().split("/");
            String strDate=date[0]+"|"+date[1];

            tv_date.setText(strDate);

            iv_disable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isRunning) {
                        if (isPreviousMilestoneComleted(pos)) {
                            if (isDateAfterORSame(pos)) {
                                if (milestone.getMilestone_iscomplete() == 0) {
                                    if (milestone.isPlayed()) {
                                        openUpdateDialog(milestone, getAdapterPosition(), iv_disable);
                                    } else {
                                        playAnimation(milestone, pos, "anim_first.json");
                                    }
                                } else {
                                    Toast.makeText(context, "milestone already completed", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(context, "Goal not yet started", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "please complete previous milestone", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });


            if (milestone.getMilestone_iscomplete() == 1) {
                lotti.setAnimation("filled_first.json");
                lotti.playAnimation();

                tv_date.setBackgroundResource(R.drawable.bg_rect_purple);
                tv_date.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));

                tv_text.setBackgroundResource(R.drawable.bg_rect_purple);
                tv_text.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));

                switch (milestone.getMilestoneStatus()) {
                    case Motionpathadapter.COMPLETED:
                    case Motionpathadapter.PARTIALLY_COMPLETED:
                        iv_disable.setImageResource(R.drawable.ic_enabled);
                        break;
                    case Motionpathadapter.NOT_COMPLETED:
                        iv_disable.setImageResource(R.drawable.ic_close_enabled);
                        break;
                }

            } else if (milestone.isPlayed()) {
                lotti.setAnimation("filled_first.json");
                lotti.playAnimation();


                tv_date.setBackgroundResource(R.drawable.bg_rect_purple);
                tv_date.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));

                tv_text.setBackgroundResource(R.drawable.bg_rect_purple);
                tv_text.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));
                iv_disable.setImageResource(R.drawable.ic_unfilled_enabled);
            }
            if (isPreviousMilestoneComleted(pos)) {
                if (isDateAfterORSame(pos)) {
                    if (milestone.getMilestone_iscomplete() == 0) {
                        if (!milestone.isPlayed()) {
                            playAnimation(milestone, pos, "anim_first.json");
                        }
                    }
                }
            } else {
                lotti.setAnimation("dashed_first.json");
                lotti.playAnimation();

                tv_date.setBackgroundResource(R.drawable.bg_rect_gray);
                tv_date.setTextColor(context.getResources().getColor(R.color.colorDarkGray));

                tv_text.setBackgroundResource(R.drawable.bg_rect_gray);
                tv_text.setTextColor(context.getResources().getColor(R.color.colorDarkGray));
                iv_disable.setImageResource(R.drawable.ic_disabled);
            }
        }


        public void openUpdateDialog(final MilestoneModel milestoneModel, final int pos, final ImageView iv_disable) {

            if (milestoneModel.getMilestone_iscomplete() == 0) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                        .setIcon(R.drawable.ic_enabled)
                        .setTitle("Milestone " + milestoneModel.getMilestoneText());

                final View customLayout
                        = context.getLayoutInflater().inflate(R.layout.layout_update_milestone, null);
                alertDialog.setView(customLayout);
                final AlertDialog dialog = alertDialog.create();

                Button btn_complete = customLayout.findViewById(R.id.btn_complete);
                Button btn_notcomplete = customLayout.findViewById(R.id.btn_notcomplete);
                Button btn_partial = customLayout.findViewById(R.id.btn_partial);

                btn_complete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        milestoneModel.setMilestone_iscomplete(1);
                        milestoneModel.setMilestoneStatus(COMPLETED);
                        milestoneModel.setMilestoneTime(System.currentTimeMillis()+"");
//                     notifyItemChanged(pos);
                        iv_disable.setImageResource(R.drawable.ic_enabled);
                        msinterface.onmilestoneUpdate(milestoneModel);

                        notifyItemChanged(pos+1);

                    }
                });

                btn_notcomplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        milestoneModel.setMilestone_iscomplete(1);
                        milestoneModel.setMilestoneStatus(NOT_COMPLETED);
                        milestoneModel.setMilestoneTime(System.currentTimeMillis()+"");
//                     notifyItemChanged(pos);
                        iv_disable.setImageResource(R.drawable.ic_close_enabled);
                        msinterface.onmilestoneUpdate(milestoneModel);

                        notifyItemChanged(pos+1);
                    }
                });

                btn_partial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        milestoneModel.setMilestone_iscomplete(1);
                        milestoneModel.setMilestoneStatus(PARTIALLY_COMPLETED);
                        milestoneModel.setMilestoneTime(System.currentTimeMillis()+"");
//                     notifyItemChanged(pos);
                        iv_disable.setImageResource(R.drawable.ic_enabled);
                        msinterface.onmilestoneUpdate(milestoneModel);

                        notifyItemChanged(pos+1);
                    }
                });
                dialog.show();
            } else {
                Toast.makeText(context, "Milestone is already completed", Toast.LENGTH_SHORT).show();
            }

        }

        private void playAnimation(final MilestoneModel milestoneModel,final int pos,final String animName) {
            lotti.setAnimation(animName);
            lotti.playAnimation();
            lotti.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    isRunning = true;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    milestoneModel.setPlayed(true);

//                notifyItemChanged(pos);
                    isRunning = false;

                    tv_date.setBackgroundResource(R.drawable.bg_rect_purple);
                    tv_date.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));

                    tv_text.setBackgroundResource(R.drawable.bg_rect_purple);
                    tv_text.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));
                    iv_disable.setImageResource(R.drawable.ic_unfilled_enabled);


                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    isRunning = false;
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
        }

    }




    public void playCoinAnimation(final LottieAnimationView coin, final String coinFile) {
        coin.setAnimation(coinFile);
        coin.playAnimation();
        coin.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isRunning = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                isRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
    }


    class ViewHolderEven extends RecyclerView.ViewHolder {

        LottieAnimationView lotti, coin;
        TextView tv_date, tv_text;
        ImageView iv_disable;


        public ViewHolderEven(@NonNull View itemView) {
            super(itemView);
            lotti = itemView.findViewById(R.id.lotti);
            coin = itemView.findViewById(R.id.coin);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_text = itemView.findViewById(R.id.tv_text);
            iv_disable = itemView.findViewById(R.id.iv_disable);
        }

        public void setData(final MilestoneModel milestone, final int pos) {

            if (pos == milestoneModels.size() - 1) {
                coin.setVisibility(View.VISIBLE);
                if (milestone.getMilestone_iscomplete() == 1 || milestone.isPlayed()) {
                    coin.setAnimation("filled_even_coin.json");
                    coin.playAnimation();
                } else {
                    coin.setAnimation("dashed_even_coin.json");
                    coin.playAnimation();
                }
            } else {
                coin.setVisibility(View.GONE);
            }

            tv_text.setText("MS" + milestone.getMilestoneNumber() + ":" + milestone.getMilestoneText());

            String date[]=milestone.getMilestoneStartdate().split("/");
            String strDate=date[0]+"|"+date[1];

            tv_date.setText(strDate);

            iv_disable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isRunning) {
                        if (isPreviousMilestoneComleted(pos)) {

                            if (milestone.getMilestone_iscomplete() == 0) {
                                if (milestone.isPlayed()) {
                                    openUpdateDialog(milestone, getAdapterPosition(), iv_disable,"anim_even_coin.json");
                                } else {
                                    playAnimation(milestone, pos, "anim_even.json" );
                                }
                            } else {
                                Toast.makeText(context, "milestone already completed", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(context, "please complete previous milestone", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });


            if (milestone.getMilestone_iscomplete() == 1) {
                lotti.setAnimation("filled_even.json");
                lotti.playAnimation();

                tv_date.setBackgroundResource(R.drawable.bg_rect_purple);
                tv_date.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));

                tv_text.setBackgroundResource(R.drawable.bg_rect_purple);
                tv_text.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));

                switch (milestone.getMilestoneStatus()) {
                    case Motionpathadapter.COMPLETED:
                    case Motionpathadapter.PARTIALLY_COMPLETED:
                        iv_disable.setImageResource(R.drawable.ic_enabled);
                        break;
                    case Motionpathadapter.NOT_COMPLETED:
                        iv_disable.setImageResource(R.drawable.ic_close_enabled);
                        break;
                }

            } else if (milestone.isPlayed()) {
                lotti.setAnimation("filled_even.json");
                lotti.playAnimation();


                tv_date.setBackgroundResource(R.drawable.bg_rect_purple);
                tv_date.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));

                tv_text.setBackgroundResource(R.drawable.bg_rect_purple);
                tv_text.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));
                iv_disable.setImageResource(R.drawable.ic_unfilled_enabled);
            }
            if (isPreviousMilestoneComleted(pos)) {
                if (milestone.getMilestone_iscomplete() == 0) {
                    if (!milestone.isPlayed()) {
                        playAnimation(milestone, pos, "anim_even.json");
                    }
                }
            } else {
                lotti.setAnimation("dashed_even.json");
                lotti.playAnimation();

                tv_date.setBackgroundResource(R.drawable.bg_rect_gray);
                tv_date.setTextColor(context.getResources().getColor(R.color.colorDarkGray));

                tv_text.setBackgroundResource(R.drawable.bg_rect_gray);
                tv_text.setTextColor(context.getResources().getColor(R.color.colorDarkGray));
                iv_disable.setImageResource(R.drawable.ic_disabled);
            }
        }


        public void openUpdateDialog(final MilestoneModel milestoneModel, final int pos, final ImageView iv_disable, final String coinFile) {

            if (milestoneModel.getMilestone_iscomplete() == 0) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                        .setIcon(R.drawable.ic_enabled)
                        .setTitle("Milestone " + milestoneModel.getMilestoneText());

                final View customLayout
                        = context.getLayoutInflater().inflate(R.layout.layout_update_milestone, null);
                alertDialog.setView(customLayout);
                final AlertDialog dialog = alertDialog.create();

                Button btn_complete = customLayout.findViewById(R.id.btn_complete);
                Button btn_notcomplete = customLayout.findViewById(R.id.btn_notcomplete);
                Button btn_partial = customLayout.findViewById(R.id.btn_partial);

                btn_complete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        milestoneModel.setMilestone_iscomplete(1);
                        milestoneModel.setMilestoneStatus(COMPLETED);
                        milestoneModel.setMilestoneTime(System.currentTimeMillis()+"");
//                    notifyItemChanged(pos);
                        iv_disable.setImageResource(R.drawable.ic_enabled);
                        msinterface.onmilestoneUpdate(milestoneModel);

                        notifyItemChanged(pos+1);

                        if (coin != null) {
                            if (coin.getVisibility() == View.VISIBLE) {
                                playCoinAnimation(coin, coinFile);
                            }
                        }
                    }
                });

                btn_notcomplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        milestoneModel.setMilestone_iscomplete(1);
                        milestoneModel.setMilestoneStatus(NOT_COMPLETED);
                        milestoneModel.setMilestoneTime(System.currentTimeMillis()+"");
//                    notifyItemChanged(pos);
                        iv_disable.setImageResource(R.drawable.ic_close_enabled);
                        msinterface.onmilestoneUpdate(milestoneModel);
                        if (coin != null) {
                            if (coin.getVisibility() == View.VISIBLE) {
                                playCoinAnimation(coin, coinFile);
                            }
                        }
                    }
                });

                btn_partial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        milestoneModel.setMilestone_iscomplete(1);
                        milestoneModel.setMilestoneStatus(PARTIALLY_COMPLETED);
                        milestoneModel.setMilestoneTime(System.currentTimeMillis()+"");
                        notifyItemChanged(pos);
                        msinterface.onmilestoneUpdate(milestoneModel);

                        if (coin != null) {
                            if (coin.getVisibility() == View.VISIBLE) {
                                playCoinAnimation(coin, coinFile);
                            }
                        }
                    }
                });
                dialog.show();
            } else {
                Toast.makeText(context, "Milestone is already completed", Toast.LENGTH_SHORT).show();
            }

        }

        private void playAnimation(final MilestoneModel milestoneModel,final int pos, final String animName) {
            lotti.setAnimation(animName);
            lotti.playAnimation();
            lotti.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    isRunning = true;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    milestoneModel.setPlayed(true);

                    tv_date.setBackgroundResource(R.drawable.bg_rect_purple);
                    tv_date.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));

                    tv_text.setBackgroundResource(R.drawable.bg_rect_purple);
                    tv_text.setTextColor(context.getResources().getColor(R.color.colorDarkPurple));
                    iv_disable.setImageResource(R.drawable.ic_unfilled_enabled);

//                notifyItemChanged(pos);
                    isRunning = false;



                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    isRunning = false;
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
        }
    }

    public boolean isPreviousMilestoneComleted(int pos) {
        boolean isAllCompleted = true;
        if (pos == 0) {
        } else if (milestoneModels.get(pos - 1).getMilestone_iscomplete() == 0) {
            isAllCompleted = false;
        }
        return isAllCompleted;
    }

    public boolean isDateAfterORSame(int pos) {
        boolean isAfter = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {

            String data = sdf.format(new Date());
            Date d1 = sdf.parse(data);
            Date d2 = sdf.parse(milestoneModels.get(pos).getMilestoneStartdate());

            if(d1.after(d2)) {
                isAfter=true;
            }
            else if(d1.before(d2)) {
                isAfter=false;
            }
            else if(d1.equals(d2)) {
                isAfter=true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isAfter;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 2;
        } else {
            return position % 2 == 0 ? 0 : 1;
        }
    }

    @Override
    public int getItemCount() {
        return milestoneModels.size();
    }

    public interface MilestoneInterface {
        void onmilestoneUpdate(MilestoneModel milestoneModel);
    }

}
