package mehmetali.com.notdefterim;

public class DataEvent {

    public static class NotEkleDialog {

        private int trigger;

        public NotEkleDialog(int trigger) {
            this.trigger = trigger;
        }

        public int getTrigger() {
            return trigger;
        }

        public void setTrigger(int trigger) {
            this.trigger = trigger;
        }
    }

    public static class UpdateList {


        private int trigger;

        public UpdateList(int trigger) {
            this.trigger = trigger;
        }

        public int getTrigger() {
            return trigger;
        }

        public void setTrigger(int trigger) {
            this.trigger = trigger;
        }
    }

    public static class SwappedNot {

        private int position;

        public SwappedNot(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    public static class DialogComplete{
        private int position;

        public DialogComplete(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    public static class DialogFragmentComplete{

        private int position;

        public DialogFragmentComplete(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    public static class DialogFragmentDatabase{
        private int position;

        public DialogFragmentDatabase(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
