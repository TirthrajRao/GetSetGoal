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

import java.util.List;

public class Motionpathadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<MilestoneModel> milestoneModels;
    MilestoneInterface msinterface;
    Activity context;

    boolean isRunning=false;

    public Motionpathadapter(Activity context, List<MilestoneModel> milestoneModels, MilestoneInterface msinterface) {
        this.milestoneModels = milestoneModels;
        this.msinterface = msinterface;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_evenitem, parent, false);
            return new ViewHolderEven(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_odditem, parent, false);
            return new ViewHolderOdd(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final MilestoneModel milestone = milestoneModels.get(position);

        if (holder.getItemViewType() == 0) {
            final ViewHolderEven even = (ViewHolderEven) holder;
            even.setData(milestone,position);

        } else {
            ViewHolderOdd odd = (ViewHolderOdd) holder;
            odd.setData(milestone,position);

        }
    }

    class ViewHolderOdd extends RecyclerView.ViewHolder {

        ImageView iv_milestoneodd,iv_start;
        TextView tvmstitleodd,tvmsmessageodd;
        LottieAnimationView iv_lotiodd;

        public ViewHolderOdd(@NonNull View itemView) {
            super(itemView);
            iv_milestoneodd=itemView.findViewById(R.id.iv_milestoneodd);
            iv_start=itemView.findViewById(R.id.iv_start);
            tvmstitleodd=itemView.findViewById(R.id.tvmstitleodd);
            tvmsmessageodd=itemView.findViewById(R.id.tvmsmessageodd);
            iv_lotiodd=itemView.findViewById(R.id.iv_lotiodd);
        }

        public void setData(final MilestoneModel milestone,int pos) {
           tvmstitleodd.setText("MS"+milestone.getMilestoneNumber()+"");
           tvmsmessageodd.setText(milestone.getMilestoneText());
           iv_milestoneodd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isRunning) {
                       // openUpdateDialog(milestone,getAdapterPosition(),iv_lotiodd, "anim_odd.json");
                        openUpdateDialog(milestone,getAdapterPosition(),iv_lotiodd, "animodd.json");
                    }
                }
            });

            if (milestone.getMilestone_iscomplete() == 1) {
                iv_milestoneodd.setImageResource(R.drawable.ic_enabled);
             //   iv_lotiodd.setAnimation("filled_even.json");
                iv_lotiodd.setAnimation("filleven.json");
                iv_lotiodd.playAnimation();
            } else {
                iv_milestoneodd.setImageResource(R.drawable.ic_disabled);
              //  iv_lotiodd.setAnimation("dashed_even.json");
                iv_lotiodd.setAnimation("dasheven.json");
                iv_lotiodd.playAnimation();
            }

            iv_start.setVisibility(milestone.getMilestoneNumber()==1 ? View.VISIBLE : View.GONE);
        }
    }

    public void openUpdateDialog(final MilestoneModel milestoneModel,final int pos, final LottieAnimationView iv_milestone, final String animName) {

        if (milestoneModel.getMilestone_iscomplete() ==0) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                    .setIcon(R.drawable.ic_enabled)
                    .setTitle("Milestone " + milestoneModel.getMilestoneText());

            final View customLayout
                    = context.getLayoutInflater().inflate(R.layout.layout_update_milestone,null);
            alertDialog.setView(customLayout);
           final AlertDialog dialog=alertDialog.create();

            Button btn_complete=customLayout.findViewById(R.id.btn_complete);
            Button btn_notcomplete=customLayout.findViewById(R.id.btn_notcomplete);
            Button btn_partial=customLayout.findViewById(R.id.btn_partial);

            btn_complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                   playAnimation(milestoneModel,iv_milestone,pos,animName);
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
                    playAnimation(milestoneModel,iv_milestone,pos,animName);
                }
            });

            dialog.show();
        } else {
            Toast.makeText(context, "Milestone is already completed", Toast.LENGTH_SHORT).show();
        }

    }

    private void playAnimation(final MilestoneModel milestoneModel,final LottieAnimationView iv_milestone,final int pos, final String animName) {
        iv_milestone.setAnimation(animName);
        iv_milestone.playAnimation();
        iv_milestone.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isRunning=true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                milestoneModel.setMilestone_iscomplete(1);
                notifyItemChanged(pos);
                msinterface.onmilestoneUpdate(milestoneModel);
                isRunning=false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                isRunning=false;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
    }



    class ViewHolderEven extends RecyclerView.ViewHolder {

        ImageView iv_milestoneeven,iv_start;
        TextView tvmstitle, tvmsmessage;
        LottieAnimationView iv_milestone;

        public ViewHolderEven(@NonNull View itemView) {
            super(itemView);
            iv_milestoneeven = itemView.findViewById(R.id.iv_milestoneeven);
            iv_start = itemView.findViewById(R.id.iv_start);
            tvmstitle = itemView.findViewById(R.id.tvmstitle);
            tvmsmessage = itemView.findViewById(R.id.tvmsmessage);
            iv_milestone = itemView.findViewById(R.id.iv_milestone);
        }

        public void setData(final MilestoneModel milestone,int pos) {
           tvmstitle.setText("MS" + milestone.getMilestoneNumber() + "");
           tvmsmessage.setText(milestone.getMilestoneText());
            iv_milestoneeven.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isRunning) {
                       // openUpdateDialog(milestone,getAdapterPosition(),iv_milestone, "anim_even.json");
                        openUpdateDialog(milestone,getAdapterPosition(),iv_milestone, "animeven.json");
                    }
                }
            });

            if (milestone.getMilestone_iscomplete() == 1) {
                iv_milestoneeven.setImageResource(R.drawable.ic_enabled);
               // iv_milestone.setAnimation("filled_odd.json");
                iv_milestone.setAnimation("fillodd.json");
                iv_milestone.playAnimation();
            } else {
                iv_milestoneeven.setImageResource(R.drawable.ic_disabled);
               // iv_milestone.setAnimation("dashed_odd.json");
                iv_milestone.setAnimation("dashodd.json");
                iv_milestone.playAnimation();
            }

            iv_start.setVisibility(milestone.getMilestoneNumber()==1 ? View.VISIBLE : View.GONE);

        }

    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return milestoneModels.size();
    }

    public interface MilestoneInterface {
        void onmilestoneUpdate(MilestoneModel milestoneModel);
    }

}
