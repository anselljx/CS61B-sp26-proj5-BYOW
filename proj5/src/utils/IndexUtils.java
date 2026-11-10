package utils;

public class IndexUtils {
    private int x;
    private int y;
    public IndexUtils(int x,int y){
      this.x = x;
      this.y = y;
    }

    public int getX(){
      return this.x;
    }

    public int getY(){
      return this.y;
    }

    public IndexUtils Move(int movX,int movY){
      return new IndexUtils(this.getX() + movX, this.getY() + movY);
    }
}
