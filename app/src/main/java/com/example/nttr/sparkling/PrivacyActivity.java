package com.example.nttr.sparkling;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class PrivacyActivity extends Activity implements View.OnClickListener{

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        textView = (TextView) findViewById(R.id.priv_text);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.setText("個人情報の取り扱いについて\n\n" +
                        "当社のアプリケーションでは、オンラインランキング表示のため、お客様の個人情報（「ひとりであそぶ」モードではゲームのスコア・ご入力頂いたユーザーネーム、「みんなであそぶ」モードではそれらに加えて位置情報・ゲームを遊んだ日時）を収集させていただくことがございます。\n\n" +
                "当社は、お客様からご提供いただく個人情報を、適切に保護することが重要であると認識し、経済産業省「個人情報の保護に関する法律についてのガイドライン」に基き、以下のように個人情報保護方針を定めます。\n\n"+
                "個人情報保護方針\n\n"+

                "１．個人情報は適切に利用します\n\n"+

                "個人情報を本人の意思に反して収集、利用することは、権利の侵害になると共に、事業者としての信頼を失うことになります。そのため、個人情報の収集、利用等のルールを明文化し、適切な取扱いを行います。また、法的な要請を除き、本人の許可なく第三者に情報を開示・提供することはありません。\n\n"+

                "２．個人情報の厳正な管理を行います\n\n"+

                "個人情報の紛失、破壊、改ざん、および漏えい等を防止するため、不正アクセス対策、ウイルス対策等の情報セキュリティ対策を行い、適切に管理いたします。また、不要になった個人情報はすみやかに廃棄いたします。\n\n"+

                "３．法令およびその他の規範を遵守します\n\n"+

                "個人情報の取り扱いに関して、個人情報保護法をはじめとする個人情報に関する法令およびその他の規範を遵守します。\n\n"+

                "４．個人情報保護方針の継続的改善を行います\n\n"+

                "ここで定めた個人情報保護方針は、定期的に見直しを行い、継続的な改善を行います。また、当社の個人情報保護方針とその運営に関して、疑問に思われる点や不都合な点等がありましたら、下記の問合わせ窓口までご連絡ください。当社で検討を行い、適切に改善を行います。\n\n"+

                "本方針および当社の個人情報の取り扱いに関するお問合せ窓口\n"+
                "電子メール: hoppingroppo@gmail.com\n");
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }
}
