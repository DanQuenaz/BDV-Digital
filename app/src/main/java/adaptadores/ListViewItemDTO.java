package adaptadores;


import android.graphics.Color;

public class ListViewItemDTO {

    private boolean checked = false;

    private String itemText = "";

    private Integer cor = Color.BLACK;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public void setItemColor(Integer color){
        this.cor = color;
    }

    public Integer getItemColor(){
        return this.cor;
    }
}