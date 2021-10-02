package com.example.attendance_tracking.utils;

/**
 * 参考
 * https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28
 * https://beightlyouch.com/blog/programming/recycler-view-swipe-delete/
 */

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE;
import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;
import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static androidx.recyclerview.widget.ItemTouchHelper.RIGHT;

import com.example.attendance_tracking.R;
import com.example.attendance_tracking.View.EditEmployeeActivity;
import com.example.attendance_tracking.View.EmployeeListAdapter;
import com.example.attendance_tracking.View.EmployeeSearchView;

import com.example.attendance_tracking.View.EditEmployeeActivity.FragNum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// TODO: create action when BehindButton clicked


public abstract class SwipeController extends ItemTouchHelper.Callback {

    private static final String TAG = "SwipeController";

    enum ButtonsState {
        GONE,
        LEFT_VISIBLE,
        RIGHT_VISIBLE
    }

    static private SwipeController pointer = null;

    private boolean swipeBack = false;
    private ButtonsState buttonShowedState = ButtonsState.GONE;
    private ButtonsState startState = ButtonsState.GONE;
    private static final float buttonWidth = 300;
    private RectF buttonInstance;
    private ImageButton deleteButton = null;
//    private RecyclerView.ViewHolder currentViewHolder = null;
    private EmployeeListAdapter.EmployeeViewHolder currentViewHolder = null;
    private ButtonsClickListener clickedListener = null;

    private boolean Hanged = false;
    private boolean swiped = false;
    private boolean moved = false;
    private RecyclerView view = null;
    private int adjust = 0;
    private LinearLayout parent = null;
    private Context mContext = null;
    private int over_center = 1;
    private boolean overCenter = false;

    private EmployeeListAdapter.OnItemClickListener itemClickListener;
    private EmployeeListAdapter.OnDeleteButtonsClickListener deleteButtonsClickListener;


//    public SwipeController(ButtonsClickListener listener){
//        this.clickedListener = listener;
//    }
    private void setListeners(){
        EmployeeListAdapter adapter = (EmployeeListAdapter) view.getAdapter();
        if(adapter == null) throw new NullPointerException("Adapter is Null Pointer");
        adapter.setOnItemClickListener(itemClickListener);
        adapter.setOnDeleteButtonsClickListener(deleteButtonsClickListener);
    }


    public SwipeController(Context context, RecyclerView recyclerView){
        pointer = this;

        mContext = context;

        view = recyclerView;
        if(view.getAdapter() == null) {
            throw new NullPointerException(view.getClass().getName() + " has no Adapter");
        }

        itemClickListener = new EmployeeListAdapter.OnItemClickListener() {
            @Override
            public void onClick(EmployeeListAdapter.EmployeeViewHolder viewHolder) {
                Log.d(TAG, "[onClick] : viewHolder");
                int pos = viewHolder.getAdapterPosition();
                if(Hanged){
                    view.getAdapter().notifyItemChanged(pos);
                    Hanged = swipeBack = false;
                    buttonShowedState = ButtonsState.GONE;
                }
                else{
                    String name = (String) viewHolder.employeeName.getText();
                    Log.d(TAG, name);
//                    ((EditEmployeeActivity)mContext).employee = viewHolder.get();
                    ((EditEmployeeActivity)mContext).employee = ((EmployeeListAdapter)view.getAdapter()).getEmployee(pos);
                    ((EditEmployeeActivity)mContext).getViewPager().setCurrentItem(FragNum.EditEmployee.get(),false);
                }
            }
        };
        deleteButtonsClickListener = new EmployeeListAdapter.OnDeleteButtonsClickListener() {
            @Override
            public void onClick(EmployeeListAdapter.EmployeeViewHolder viewHolder) {
                Log.d(TAG, "[onClick] delete button: " + view.getClass().getName());
                int pos = viewHolder.getAdapterPosition();
                EmployeeListAdapter adapter = (EmployeeListAdapter) view.getAdapter();
                new AlertDialog.Builder(context)
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .setTitle("確認")
                        .setMessage("従業員データが完全に削除されます．")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(context instanceof EditEmployeeActivity){
                                    ((EditEmployeeActivity) context).getHomeFragment()
                                            .requestRemoveEmployee(adapter.getEmployee(pos));
                                }
                                adapter.remove(pos);
                            }
                        })
                        .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                view.getAdapter().notifyItemChanged(pos);
                                swipeBack = Hanged = false;
                                buttonShowedState = ButtonsState.GONE;
                            }
                        })
                        .show();
            }

        };
        setListeners();

    }

    static public SwipeController getPointer() { return pointer; }

    @Override
    public int getMovementFlags(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder) {
        Log.d(TAG, "[getMovementFlags]");
        view = recyclerView;
//        setListeners();


        switch(buttonShowedState){
            case LEFT_VISIBLE:
                adjust = 1;
                startState = ButtonsState.LEFT_VISIBLE;
                break;
            case RIGHT_VISIBLE:
                adjust = -1;
                startState = ButtonsState.RIGHT_VISIBLE;
                break;
            default:
                adjust = 0;
                startState = ButtonsState.GONE;
        }


        return makeMovementFlags(0, LEFT|RIGHT);
    }

    @Override
    public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
        Log.d(TAG, "[onMoved]");
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
    }

    @Override
    public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
        Log.d(TAG, "[onSwiped]");
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return view.getWidth()*1.0f;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        Log.d(TAG, "Velocity default Value: "+defaultValue);
        // the smaller this velocity is, the harder item is considered as swiped
        return super.getSwipeVelocityThreshold(defaultValue*0.0001f);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c,
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        setListeners();
        if(currentViewHolder!=null && currentViewHolder != viewHolder){
            getDefaultUIUtil().onDraw(c,recyclerView,
                    ((EmployeeListAdapter.EmployeeViewHolder)currentViewHolder).foreground,
                    0,0,actionState,isCurrentlyActive);
            Hanged = swipeBack = false;
            buttonShowedState = ButtonsState.GONE;
        }
        currentViewHolder = (EmployeeListAdapter.EmployeeViewHolder) viewHolder;
        float ad = 0;
        float pre_dX = 0;
        if (viewHolder instanceof EmployeeListAdapter.EmployeeViewHolder) {
            currentViewHolder = (EmployeeListAdapter.EmployeeViewHolder) viewHolder;
        }
        else{
            try {
                throw new InstantiationException();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(actionState == ACTION_STATE_IDLE){
            Log.d(TAG, "ACTION_STATE_IDLE");
        }

        if(actionState == ACTION_STATE_SWIPE){
            Log.d(TAG, "[dX,dY] ["+dX+","+dY+"] "+(Hanged ? " Hanged ":"")+(buttonShowedState==ButtonsState.GONE?"GONE ":(buttonShowedState==ButtonsState.LEFT_VISIBLE?"LEFT":"RIGHT")));
            pre_dX = dX;
            if (buttonShowedState != ButtonsState.GONE) {

                if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
                    if (Hanged) {
                        int code = adjust;
                        if(overCenter) code *= -1;
                        ad = code*buttonWidth;
                    }
                    if(!moved){
                        if(!swipeBack)  dX = Math.min(dX + ad, buttonWidth);
                        else            dX = Math.max(dX + ad, buttonWidth);
                    }
                    else                dX = dX + ad;

                }
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
                    if (Hanged) {
                        int code = adjust;
                        if(overCenter) code *= -1;
                        ad = code*buttonWidth;
                    }
                    if(!moved) {
                        if(!swipeBack)  dX = Math.max(dX + ad, -buttonWidth);
                        else            dX = Math.min(dX + ad, -buttonWidth);
                    }
                    else                dX = dX+ad;
                }

//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                getDefaultUIUtil().onDraw(c,recyclerView,((EmployeeListAdapter.EmployeeViewHolder)viewHolder).foreground,
                        dX,dY,actionState,isCurrentlyActive);
            }

        }
        if (buttonShowedState == ButtonsState.GONE) {
            if(swipeBack){
//                pos = viewHolder.getAdapterPosition();
//                recyclerView.getAdapter().notifyItemChanged(pos);
                getDefaultUIUtil().onDraw(c,recyclerView,((EmployeeListAdapter.EmployeeViewHolder)viewHolder).foreground,
                        0,0,actionState,isCurrentlyActive);
            }
            else {
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                getDefaultUIUtil().onDraw(c,recyclerView,((EmployeeListAdapter.EmployeeViewHolder)viewHolder).foreground,
                        dX,dY,actionState,isCurrentlyActive);
            }
        }

        getDefaultUIUtil().onDraw(c,recyclerView,
                ((EmployeeListAdapter.EmployeeViewHolder)viewHolder).background,
                0,0,actionState,isCurrentlyActive);

        if(!moved && pre_dX==0) {
            swipeBack = Hanged;
            over_center = adjust;
            overCenter = false;
        }
//        int pos = currentViewHolder.getAdapterPosition();
//        deleteButton = (EmployeeListAdapter) recyclerView.getAdapter().

//        setOnButtons(null, currentViewHolder.deleteButton);

//        drawBehindButtons(c, (EmployeeListAdapter.EmployeeViewHolder) viewHolder, Objects.requireNonNull(buffer), pos, dX);
//        drawButtons(c, viewHolder);
        setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        swiped = false;
    }

    private void setOnButtons(ImageButton leftButton, ImageButton rightButton)
    {
        if(leftButton != null){
            clickedListener.setOnLeftListener(leftButton);
        }
        if(rightButton != null) {
            clickedListener.setOnRightListener(rightButton);
        }
    }

    public void onDraw(Canvas c){
        if(currentViewHolder != null) drawButtons(c, currentViewHolder);
    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithoutPadding = buttonWidth - 20;
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop()+10, itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom()-20);
        p.setColor(Color.BLUE);
        c.drawRoundRect(leftButton, corners, corners, p);
        drawText("編集", c, leftButton, p);

        RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop()+10, itemView.getRight(), itemView.getBottom()-20);
        p.setColor(Color.RED);
        c.drawRoundRect(rightButton, corners, corners, p);
        // TODO: set Icon on Canvas
//        Bitmap bitmap = ((BitmapDrawable) Objects.requireNonNull(getDrawable(R.drawable.delete_outline))).getBitmap();
//        Bitmap bitmap = BitmapFactory.decodeResource(itemView.getContext().getResources(), R.drawable.delete_outline);
//        c.drawBitmap(bitmap, rightButton.centerX() - (bitmap.getWidth()/2), rightButton.centerY() - (bitmap.getHeight()/2), null);
//        Drawable icon = ContextCompat.getDrawable(itemView.getContext(), R.drawable.delete_outline);
//        Drawable icon = itemView.getResources().getDrawable(R.drawable.delete_outline, null);
//        c.save();
//        icon.draw(c);
//        c.restore();
        drawText("削除", c, rightButton, p);


        buttonInstance = null;
        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
            buttonInstance = leftButton;
        }
        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
            buttonInstance = rightButton;
        }
    }

    private void drawBehindButtons(Canvas c, EmployeeListAdapter.EmployeeViewHolder viewHolder, List<BehindButton> buffer, int pos, float dX){
        float right = viewHolder.itemView.getRight();
        float dButtonWidth = (-1) * dX / buffer.size();

        for (BehindButton button : buffer) {
            float left = right - dButtonWidth;
            button.onDraw(c, viewHolder);
            right = left;
        }
    }

    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 60;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p);
    }

    private void setTouchListener(final Canvas c,
                                  final RecyclerView recyclerView,
                                  final RecyclerView.ViewHolder viewHolder,
                                  final float dX, final float dY,
                                  final int actionState, final boolean isCurrentlyActive)
    {
        recyclerView.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                int pos = viewHolder.getAdapterPosition();
//                List<BehindButton> buffer = buttonBuffer.get(pos);
//                if(buffer != null && !moved) {
//                    Log.d(TAG, "[onDraw]");
//                    drawBehindButtons(c, (EmployeeListAdapter.EmployeeViewHolder) viewHolder, buffer, pos, 0);
//                    SwipeController.super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//                }

                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "Down");
                        break;

                    case MotionEvent.ACTION_MOVE:
                    {
                        moved = true;
//                        if(!Hanged){
//                            if (dX >= buttonWidth)      buttonShowedState = ButtonsState.LEFT_VISIBLE;
//                            else if (dX <= -buttonWidth) buttonShowedState = ButtonsState.RIGHT_VISIBLE;
//                            else                        buttonShowedState = ButtonsState.GONE;
//                        }
//                        else{
//                            if (adjust*dX < 0)  buttonShowedState = ButtonsState.GONE;
//                            else                buttonShowedState = startState;
//                        }

                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                    {
                        Log.d(TAG, "Released");

                        if(!Hanged){
                            if (dX >= buttonWidth)      buttonShowedState = ButtonsState.LEFT_VISIBLE;
                            else if (dX <= -buttonWidth) buttonShowedState = ButtonsState.RIGHT_VISIBLE;
                            else                        buttonShowedState = ButtonsState.GONE;
                        }
                        else{
                            if (adjust*dX < 0) {
                                Log.d(TAG,"dX: "+adjust*dX);
                                if(adjust*dX<-buttonWidth){
                                    over_center = adjust;
                                    overCenter = true;
                                    buttonShowedState = switchState(buttonShowedState);
                                }
                                else buttonShowedState = ButtonsState.GONE;
                            }
                            else                buttonShowedState = startState;
                        }

                        if(buttonShowedState == ButtonsState.LEFT_VISIBLE){
                            if(!Hanged && dX>=buttonWidth){ Hanged = true; }
                            if(Hanged && dX<buttonWidth) { Hanged = false; buttonShowedState = ButtonsState.GONE; }
                        }
                        if(buttonShowedState == ButtonsState.RIGHT_VISIBLE)
                        {
                            if(!Hanged && dX<=-buttonWidth){ Hanged = true; }
                            if(Hanged && dX>-buttonWidth) { Hanged = false; buttonShowedState = ButtonsState.GONE; }
                        }

                        if(buttonShowedState == ButtonsState.GONE) {
                            Hanged = false;
                            over_center = 1;
                        }

//                        if(buttonShowedState != ButtonsState.GONE)
//                            drawBehindButtons(c, (EmployeeListAdapter.EmployeeViewHolder) viewHolder, buffer, pos, 0);
                        moved = false;
//                        setItemsClickable(recyclerView, Hanged);
                        break;
                    }

                }
                return false;
            }
        });
    }

    private ButtonsState switchState(ButtonsState current){
        String prestring = "switch "+current.toString()+" to ";
        switch(current){
            case LEFT_VISIBLE:
                Log.d(TAG, prestring+"RIGHT_VISIBLE");
                return ButtonsState.RIGHT_VISIBLE;
            case RIGHT_VISIBLE:
                Log.d(TAG, prestring+"LEFT_VISIBLE");
                return ButtonsState.LEFT_VISIBLE;
            default:
                return ButtonsState.GONE;
        }
    }


    private void setItemsClickable(RecyclerView recyclerView,
                                   boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    public void setOnButtonsClickedListener(ButtonsClickListener listener){
        if(listener != null) {
            clickedListener = listener;
        }
    }

    static public class ButtonsClickListener {
        public void setOnLeftButtonClicked(View.OnClickListener listener) {
            this.onLeftButtonClickLister = listener;
        }
        public void setOnRightButtonClicked(View.OnClickListener listener){
            this.onRightButtonClickLister = listener;
        }

        public ButtonsClickListener(View.OnClickListener lListener, View.OnClickListener rListener){
            this.onLeftButtonClickLister = lListener;
            this.onRightButtonClickLister = rListener;
        }

        public void setOnLeftListener(ImageButton button){
            if(button != null) button.setOnClickListener(onLeftButtonClickLister);
        }
        public void setOnRightListener(ImageButton button){
            if(button != null) button.setOnClickListener(onRightButtonClickLister);
        }

        public void onRightButtonClicked(int position) {}
//        abstract private class onLeftButtonClickLister  implements View.OnClickListener{}
//        abstract private class onRightButtonClickLister  implements View.OnClickListener{}
        private View.OnClickListener onLeftButtonClickLister = null;
        private View.OnClickListener onRightButtonClickLister = null;

    }


    public void setInit(){
        if(view != null && currentViewHolder != null) {
            int pos = currentViewHolder.getAdapterPosition();
            view.getAdapter().notifyItemChanged(pos);
        }
        buttonShowedState = ButtonsState.GONE;
        Hanged = swipeBack = false;
    }
    public abstract void instantiateBehindButton(EmployeeListAdapter.EmployeeViewHolder viewHolder, List<BehindButton> underlayButtons);

    public static class BehindButton {
        private String text;
        private int imageResId;
        private int color;
        private int pos;
        private RecyclerView recyclerView;
        private RectF clickRegion;
        private EmployeeListAdapter.EmployeeViewHolder viewHolder;
        private BehindButtonClickListener clickListener;
        private float buttonWidthWithoutPadding = buttonWidth - 20;
        private float corners = 16;
        private Paint p;
        RectF leftButton = null, rightButton = null;


        public BehindButton(RecyclerView recyclerView, EmployeeListAdapter.EmployeeViewHolder holder, BehindButtonClickListener clickListener) {
//            this.text = text;
//            this.imageResId = imageResId;
//            this.color = color;
            this.viewHolder = holder;
            this.clickListener = clickListener;
            this.recyclerView = recyclerView;
//            p = new Paint();
//
//            View itemView = viewHolder.itemView;
//
//            leftButton = new RectF(itemView.getLeft(), itemView.getTop()+10, itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom()-20);
//            p.setColor(Color.BLUE);
//
//            rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop()+10, itemView.getRight(), itemView.getBottom()-20);
//            p.setColor(Color.RED);
//

        }

        public boolean onClick(float x, float y){
            if (leftButton.contains(x, y)){
                Log.d(TAG, "[onClick] leftButton");
                clickListener.onClick(viewHolder, pos);
                SwipeController.getPointer().setInit();
                return true;
            }
            if(rightButton.contains(x, y)){
                Log.d(TAG, "[onClick] rightButton");
                clickListener.onClick(viewHolder, pos);
                SwipeController.getPointer().setInit();
                return true;
            }

            return false;
        }

        public void onDraw(Canvas c, EmployeeListAdapter.EmployeeViewHolder viewHolder){
            p = new Paint();

            View itemView = viewHolder.itemView;

            leftButton = new RectF(itemView.getLeft(), itemView.getTop()+10, itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom()-20);
            p.setColor(Color.RED);
//            c.drawRoundRect(leftButton, corners, corners, p);
//            drawText("編集", c, leftButton, p);
            c.drawRoundRect(leftButton, corners, corners, p);
            drawText("削除", c, leftButton, p);

            rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop()+10, itemView.getRight(), itemView.getBottom()-20);
            p.setColor(Color.RED);
            c.drawRoundRect(rightButton, corners, corners, p);
            drawText("削除", c, rightButton, p);

//            clickRegion = rightButton;
            pos = viewHolder.getAdapterPosition();
        }

        private void drawText(String text, Canvas c, RectF button, Paint p) {
            float textSize = 60;
            p.setColor(Color.WHITE);
            p.setAntiAlias(true);
            p.setTextSize(textSize);

            float textWidth = p.measureText(text);
            c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p);
        }

    }

    public interface BehindButtonClickListener{
        void onClick(EmployeeListAdapter.EmployeeViewHolder viewHolder, int position);
    }





}
