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
                if (milestone.getMilestone_iscomplete()==1 || milestone.isPlayed()) {
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
            tv_date.setText(milestone.getMilestoneStartdate());

            iv_disable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isRunning) {
                        if (isPreviousMilestoneComleted(pos)) {

                            if(milestone.getMilestone_iscomplete()==0)
                            {
                                if (milestone.isPlayed()) {
                                    openUpdateDialog(milestone, getAdapterPosition(),iv_disable);
                                } else {
                                    playAnimation(milestone, lotti, pos, "anim_odd.json",coin,"anim_odd_coin.json");
                                }
                            }
                            else {
                                Toast.makeText(context, "milestone already completed", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "please complete previous milestone", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });


            if (milestone.getMilestone_iscomplete() == 1) {
                iv_disable.setImageResource(R.drawable.ic_enabled);
            } else {
                iv_disable.setImageResource(R.drawable.ic_disabled);
            }

            if (milestone.getMilestone_iscomplete()==1 || milestone.isPlayed()) {
                lotti.setAnimation("filled_odd.json");
                lotti.playAnimation();
            } else {
                lotti.setAnimation("dashed_odd.json");
                lotti.playAnimation();
            }


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
            tv_date.setText(milestone.getMilestoneStartdate());

            iv_disable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isRunning) {
                        if (isPreviousMilestoneComleted(pos)) {
                            if (isDateAfterORSame(pos)) {
                                if(milestone.getMilestone_iscomplete()==0) {
                                    if (milestone.isPlayed()) {
                                        openUpdateDialog(milestone, getAdapterPosition(),iv_disable);
                                    } else {
                                        playAnimation(milestone, lotti, pos, "anim_first.json",null,"");
                                    }
                                }
                                else {
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
                iv_disable.setImageResource(R.drawable.ic_enabled);
            } else {
                iv_disable.setImageResource(R.drawable.ic_disabled);
            }

            if (milestone.getMilestone_iscomplete()==1 || milestone.isPlayed()) {
                lotti.setAnimation("filled_first.json");
                lotti.playAnimation();
            } else {
                lotti.setAnimation("dashed_first.json");
                lotti.playAnimation();
            }
        }
    }

    public void openUpdateDialog(final MilestoneModel milestoneModel, final int pos,final ImageView iv_disable) {

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
//                    notifyItemChanged(pos);
                    iv_disable.setImageResource(R.drawable.ic_enabled);
                    msinterface.onmilestoneUpdate(milestoneModel);
                }
            });

            btn_notcomplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            btn_partial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    milestoneModel.setMilestone_iscomplete(1);
                    notifyItemChanged(pos);
                    msinterface.onmilestoneUpdate(milestoneModel);
                }
            });
            dialog.show();
        } else {
            Toast.makeText(context, "Milestone is already completed", Toast.LENGTH_SHORT).show();
        }

    }

    private void playAnimation(final MilestoneModel milestoneModel, final LottieAnimationView iv_milestone, final int pos, final String animName, final LottieAnimationView coin,final String coinFile) {
        iv_milestone.setAnimation(animName);
        iv_milestone.playAnimation();
        iv_milestone.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                milestoneModel.setPlayed(true);
//                notifyItemChanged(pos);
                isRunning = false;

                if(coin!=null) {
                    if(coin.getVisibility()==View.VISIBLE)
                    {
                        playCoinAnimation(coin,coinFile);
                    }
                }


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


   public void playCoinAnimation(final LottieAnimationView coin,final String coinFile)
   {
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
                if (milestone.getMilestone_iscomplete()==1 || milestone.isPlayed()) {
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
            tv_date.setText(milestone.getMilestoneStartdate());

            iv_disable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isRunning) {
                        if (isPreviousMilestoneComleted(pos)) {

                            if(milestone.getMilestone_iscomplete()==0) {
                                if (milestone.isPlayed()) {
                                    openUpdateDialog(milestone, getAdapterPosition(),iv_disable);
                                } else {
                                    playAnimation(milestone, lotti, pos, "anim_even.json",coin,"anim_even_coin.json");
                                }
                            }
                            else {
                                Toast.makeText(context, "milestone already completed", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(context, "please complete previous milestone", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });


            if (milestone.getMilestone_iscomplete() == 1) {
                iv_disable.setImageResource(R.drawable.ic_enabled);
            } else {
                iv_disable.setImageResource(R.drawable.ic_disabled);
            }

            if (milestone.getMilestone_iscomplete()==1 || milestone.isPlayed()) {
                lotti.setAnimation("filled_even.json");
                lotti.playAnimation();
            } else {
                lotti.setAnimation("dashed_even.json");
                lotti.playAnimation();
            }
        }
    }

    public boolean isPreviousMilestoneComleted(int pos) {
        boolean isAllCompleted = true;
        for (int i = pos - 1; i >= 0; i--) {
            if (milestoneModels.get(i).getMilestone_iscomplete() == 0) {
                isAllCompleted = false;
                break;
            }
        }
        return isAllCompleted;
    }

    public boolean isDateAfterORSame(int pos) {
        boolean isAfter = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date d1 = sdf.parse(milestoneModels.get(pos).getMilestoneStartdate());
            String data = sdf.format(new Date());
            Date d2=sdf.parse(data);

            isAfter = d1.compareTo(d2) < 0 || d1.compareTo(d2)==0;
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
