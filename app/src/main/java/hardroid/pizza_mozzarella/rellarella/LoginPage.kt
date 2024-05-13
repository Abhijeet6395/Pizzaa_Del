package hardroid.pizza_mozzarella.rellarella

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class LoginPage: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_form)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    override fun onBackPressed() {
        gotoMainActivity()
        super.onBackPressed()
    }

    private fun gotoMainActivity(){
        try {
            var intent = Intent(this@LoginPage,MainActivity::class.java)
            startActivity(intent)
            finish()
        }catch (_:Exception){}
    }
}