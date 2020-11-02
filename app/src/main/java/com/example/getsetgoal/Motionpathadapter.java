package com.example.getsetgoal;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;

import java.util.List;

public class Motionpathadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<MilestoneModel> milestoneModels;
    Milestone msinterface;
    Context context;

    public Motionpathadapter(Context context, List<MilestoneModel> milestoneModels, Milestone msinterface) {
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

//        holder.mnumber.setText("MS"+milestonedata.get(position).getMilestoneNumber()+"");
//        holder.mdetails.setText(milestonedata.get(position).getMilestoneText());
//        holder.mdays.setText(milestonedata.get(position).getMilstonedays()+"");
//        holder.mstartdate.setText(milestonedata.get(position).getMilestoneStartdate());
//        holder.menddate.setText(milestonedata.get(position).getMilestoneEnddate());
        final MilestoneModel milestone = milestoneModels.get(position);

        if (holder.getItemViewType() == 0) {
            final ViewHolderEven even = (ViewHolderEven) holder;
            even.setData(milestone);

        } else {
            ViewHolderOdd odd = (ViewHolderOdd) holder;
            odd.setData(milestone);

        }
    }

    class ViewHolderOdd extends RecyclerView.ViewHolder {

        ImageView iv_milestoneodd;
        TextView tvmstitleodd,tvmsmessageodd;
        LottieAnimationView iv_lotiodd;

        public ViewHolderOdd(@NonNull View itemView) {
            super(itemView);
            iv_milestoneodd=itemView.findViewById(R.id.iv_milestoneodd);
            tvmstitleodd=itemView.findViewById(R.id.tvmstitleodd);
            tvmsmessageodd=itemView.findViewById(R.id.tvmsmessageodd);
            iv_lotiodd=itemView.findViewById(R.id.iv_lotiodd);
        }

        public void setData(final MilestoneModel milestone) {
           tvmstitleodd.setText("MS"+milestone.getMilestoneNumber()+"");
           tvmsmessageodd.setText(milestone.getMilestoneText());
           iv_milestoneodd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    msinterface.mymilestone(milestone);
                    playAnimation(milestone,getAdapterPosition(),iv_lotiodd,"anim_odd.json","filled_odd.json",iv_milestoneodd );
                }
            });

            if (milestone.getMilestone_iscomplete() == 1) {
                iv_milestoneodd.setImageResource(R.drawable.ic_enabled);
                iv_lotiodd.setAnimation("filled_odd.json");
                iv_lotiodd.playAnimation();
            } else {
                iv_milestoneodd.setImageResource(R.drawable.ic_disabled);
                iv_lotiodd.setAnimation("dashed_odd.json");
                iv_lotiodd.playAnimation();



            }
        }
    }

    public void playAnimation(final MilestoneModel milestoneModel,final int pos, final LottieAnimationView iv_milestone, final String animName,final String updatedAnim,final ImageView iv_status) {

        if (milestoneModel.getMilestone_iscomplete() ==0) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                    .setIcon(R.drawable.ic_enabled)
                    .setTitle("Milestone " + milestoneModel.getMilestoneText())
                    .setMessage("press yes to complete milestone.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            iv_milestone.setAnimation(animName);
                            iv_milestone.playAnimation();
                            iv_milestone.addAnimatorListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    milestoneModel.setMilestone_iscomplete(1);
                                    notifyItemChanged(pos);
                                    msinterface.mymilestone(milestoneModel);
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {
                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {
                                }
                            });
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            alertDialog.show();
        } else {
            Toast.makeText(context, "Milestone is already completed", Toast.LENGTH_SHORT).show();
        }

    }


    class ViewHolderEven extends RecyclerView.ViewHolder {

        ImageView iv_milestoneeven;
        TextView tvmstitle, tvmsmessage;
        LottieAnimationView iv_milestone;

        public ViewHolderEven(@NonNull View itemView) {
            super(itemView);
            iv_milestoneeven = itemView.findViewById(R.id.iv_milestoneeven);
            tvmstitle = itemView.findViewById(R.id.tvmstitle);
            tvmsmessage = itemView.findViewById(R.id.tvmsmessage);
            iv_milestone = itemView.findViewById(R.id.iv_milestone);
        }

        public void setData(final MilestoneModel milestone) {
           tvmstitle.setText("MS" + milestone.getMilestoneNumber() + "");
           tvmsmessage.setText(milestone.getMilestoneText());
            iv_milestoneeven.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    msinterface.mymilestone(milestone);
                    playAnimation(milestone,getAdapterPosition(),iv_milestone,"anim_even.json","filled_even.json",iv_milestoneeven);
                }
            });

            if (milestone.getMilestone_iscomplete() == 1) {
                iv_milestoneeven.setImageResource(R.drawable.ic_enabled);
                iv_milestone.setAnimation("filled_even.json");
                iv_milestone.playAnimation();
            } else {
                iv_milestoneeven.setImageResource(R.drawable.ic_disabled);
                iv_milestone.setAnimation("dashed_even.json");
                iv_milestone.playAnimation();
            }

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

    public interface Milestone {
        void mymilestone(MilestoneModel milestoneModel);
    }

}
