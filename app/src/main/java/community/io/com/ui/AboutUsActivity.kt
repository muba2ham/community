package community.io.com.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import community.io.com.R
import community.io.com.databinding.ActivityAboutUsBinding

class AboutUsActivity  :AppCompatActivity(){

    var webData =
//        "<html>><body>" +
            "<div class=\"inner_cont\">\n" +
            "    <div class=\"inner_block\">\n" +
            "      <h2>About Beit Hanina</h2>\n" +
            "      <p>Beit Hanina (Arabic: بيت حنينا\u200E\u200E, Hebrew: בית חנינא\u200E\u200E) is a Palestinian \n" +
            "    neighborhood in East Jerusalem. It is on the road to Ramallah, eight \n" +
            "    kilometers north of central Jerusalem, at an elevation of 780 meters above sea level.[2] Beit Hanina is bordered by Pisgat Ze'ev and Hizma to the east, Ramot, Ramat Shlomo and Shuafat to the south, Beit Iksa and Nabi Samwil to the west, and Bir Nabala, al-Jib, Kafr Aqab and ar-Ram to the north.</p>\n" +
            "    <p>Beit Hanina is divided by the Israeli West Bank barrier into Al-Jadida (the new village), which is located within the Israeli Jerusalem municipality and includes the vast majority of the built-up area, and Al-Balad (the old village), which lies outside the municipality.[3] The total area of Beit Hanina is 16.3 sq. kilometers (6.3 sq. miles) or 16,284 dunams, of which 2,775 are built up.[4]</p>\n" +
            "    </div>\n" +
            "\n" +
            "     <div class=\"inner_block\">\n" +
            "      <h3>Etymology</h3>\n" +
            "      <p>Literally, Beit Hanina means \"House of Hanina,\" suggesting that it is named after a person, possibly a woman. Some scholars say that \"Hanina\" is derived from the Assyrian \"Han-nina\" which means \"the one who deserves pity\" (hanan). It could also be derived from the word hana meaning \"camped.\"[1][7]</p>\n" +
            "    </div>\n" +
            "    <div class=\"inner_block\">\n" +
            "      <h3>Archeology</h3>\n" +
            "      <p>In June 2013, Israel Antiquities Authority unearthed an 1800-year-old Roman road in Beit Hanina. The Jerusalem Post wrote about this discovery: \"According to the Antiquities Authority, the 8-meter-wide road, which dates back to the Roman Empire, led from Jaffa to Jerusalem and was built with large flat stones and curbstones to create a surface that was comfortable for walking. Some of the stones were highly polished, indicating heavy pedestrian use, the authority added.\" [8]</p>\n" +
            "    </div>\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "</div>\n"
//                    "</body></html>"

    lateinit var binding: ActivityAboutUsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_about_us)
        binding.lifecycleOwner = this


        binding.ivToolbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.webview.loadData(webData, "text/html", "UTF-8");
//        bindViewModel()


    }

}