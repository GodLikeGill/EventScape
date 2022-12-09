package com.group5.eventscape.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.group5.eventscape.R;

public class SettingsActivity extends AppCompatActivity {

    ImageButton back;
    CardView cvPriPol;
    CardView cvPayments;
    CardView cvHelpSup;
    CardView cvTermOfServ;
    AlertDialog.Builder alertHelp;
    AlertDialog.Builder alertPriPol;
    AlertDialog.Builder alertTermOfServ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        back = findViewById(R.id.ibBackMyEvents);
        cvPriPol = findViewById(R.id.cvPrivacyPolicy);
        cvPayments = findViewById(R.id.cvPayments);
        cvHelpSup = findViewById(R.id.cvHelpSup);
        cvTermOfServ = findViewById(R.id.cvTermOfSer);
        alertHelp = new AlertDialog.Builder(this);
        alertPriPol = new AlertDialog.Builder(this);
        alertTermOfServ = new AlertDialog.Builder(this);

        cvPayments.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PaymentsActivity.class)));
        cvHelpSup.setOnClickListener(view -> alertHelp.setTitle("FAQs")
                .setMessage("EventScape FAQs to help you plan your next event. \n" +
                        "\n" +
                        "Can I use EventScape for free events? \n" +
                        "There's no cost for organizers to use EventScape if you're not charging for tickets. There are no monthly charges, enrollment costs, or setup fees. If you're charging for ticket sales, our fees vary by package. EventScape is what you need to grow your community at your next free event.\n" +
                        "\n" +
                        "How do I get money from EventScape events?\n" +
                        "To make sure you get paid for your ticket sales, it's crucial to enter your payout details. You can be paid by direct deposit, check (USD only), PayPal, or Authorize.Net. When using EventScape Payment Processing to collect payments, your payout will start processing 4-5 days after the event ends.\n" +
                        "\n" +
                        "How much does it cost to use EventScape?\n" +
                        "You can sign up for free and avoid ticketing fees by passing them on to attendees. There are also paid solutions available.\n" +
                        "\n" +
                        "EventScape's pricing is 2% of the ticket price and $0.79 per paid ticket plus 2.5% payment processing per transaction for our essentials package.\n" +
                        "EventScape's pricing is 3.5% of the ticket price and $1.59 per paid ticket plus a 2.5% payment processing fee per transaction for our professional package.\n" +
                        "EventScape's premium package has custom pricing.\n" +
                        "\n" +
                        "How do I sell tickets on EventScape?\n" +
                        "You can accept cash or credit card payments for your event with EventScape Organizer.\n" +
                        "\n" +
                        "It's a great way to keep track of on-site sales, and you can even collect contact information for your attendees. Follow these ticket-pricing strategies to stay competitive and maximize ticket sales. Consider tiered pricing, which can make your event marketable to a broader audience, or VIP experiences for event-goers looking for more.– Review our payment processing options here.")
                .setPositiveButton("Back", (dialogInterface, i) -> {}).show());

        cvTermOfServ.setOnClickListener(view -> alertTermOfServ.setTitle("Terms of Service")
                .setMessage("By downloading or using the app, these terms will automatically apply to you – you should make sure therefore that you read them carefully before using the app. You’re not allowed to copy or modify the app, any part of the app, or our trademarks in any way. You’re not allowed to attempt to extract the source code of the app, and you also shouldn’t try to translate the app into other languages or make derivative versions. The app itself, and all the trademarks, copyright, database rights, and other intellectual property rights related to it, still belong to Work Project.\n" +
                        "\n" +
                        "Work Project is committed to ensuring that the app is as useful and efficient as possible. For that reason, we reserve the right to make changes to the app or to charge for its services, at any time and for any reason. We will never charge you for the app or its services without making it very clear to you exactly what you’re paying for.\n" +
                        "\n" +
                        "The EventScape app stores and processes personal data that you have provided to us, to provide my Service. It’s your responsibility to keep your phone and access to the app secure. We therefore recommend that you do not jailbreak or root your phone, which is the process of removing software restrictions and limitations imposed by the official operating system of your device. It could make your phone vulnerable to malware/viruses/malicious programs, compromise your phone’s security features and it could mean that the EventScape app won’t work properly or at all.\n" +
                        "\n" +
                        "The app does use third-party services that declare their Terms and Conditions.\n" +
                        "\n" +
                        "Link to Terms and Conditions of third-party service providers used by the app\n" +
                        "\n" +
                        "Google Play Services\n" +
                        "You should be aware that there are certain things that Work Project will not take responsibility for. Certain functions of the app will require the app to have an active internet connection. The connection can be Wi-Fi or provided by your mobile network provider, but Work Project cannot take responsibility for the app not working at full functionality if you don’t have access to Wi-Fi, and you don’t have any of your data allowance left.\n" +
                        "\n" +
                        "If you’re using the app outside of an area with Wi-Fi, you should remember that the terms of the agreement with your mobile network provider will still apply. As a result, you may be charged by your mobile provider for the cost of data for the duration of the connection while accessing the app, or other third-party charges. In using the app, you’re accepting responsibility for any such charges, including roaming data charges if you use the app outside of your home territory (i.e. region or country) without turning off data roaming. If you are not the bill payer for the device on which you’re using the app, please be aware that we assume that you have received permission from the bill payer for using the app.\n" +
                        "\n" +
                        "Along the same lines, Work Project cannot always take responsibility for the way you use the app i.e. You need to make sure that your device stays charged – if it runs out of battery and you can’t turn it on to avail the Service, Work Project cannot accept responsibility.\n" +
                        "\n" +
                        "With respect to Work Project’s responsibility for your use of the app, when you’re using the app, it’s important to bear in mind that although we endeavor to ensure that it is updated and correct at all times, we do rely on third parties to provide information to us so that we can make it available to you. Work Project accepts no liability for any loss, direct or indirect, you experience as a result of relying wholly on this functionality of the app.\n" +
                        "\n" +
                        "At some point, we may wish to update the app. The app is currently available on Android – the requirements for the system(and for any additional systems we decide to extend the availability of the app to) may change, and you’ll need to download the updates if you want to keep using the app. Work Project does not promise that it will always update the app so that it is relevant to you and/or works with the Android version that you have installed on your device. However, you promise to always accept updates to the application when offered to you, We may also wish to stop providing the app, and may terminate use of it at any time without giving notice of termination to you. Unless we tell you otherwise, upon any termination, (a) the rights and licenses granted to you in these terms will end; (b) you must stop using the app, and (if needed) delete it from your device.\n" +
                        "\n" +
                        "Changes to This Terms and Conditions\n" +
                        "\n" +
                        "I may update our Terms and Conditions from time to time. Thus, you are advised to review this page periodically for any changes. I will notify you of any changes by posting the new Terms and Conditions on this page.\n" +
                        "\n" +
                        "These terms and conditions are effective as of 2022-11-10\n" +
                        "\n" +
                        "Contact Us\n" +
                        "\n" +
                        "If you have any questions or suggestions about my Terms and Conditions, do not hesitate to contact me at eventscape7@gmail.com.")
                .setCancelable(false)
                .setPositiveButton("Back", (dialogInterface, i) -> {}).show());

        cvPriPol.setOnClickListener(view -> alertPriPol.setTitle("Privacy Policy")
                .setMessage("Work Project built the EventScape app as an Ad Supported app. This SERVICE is provided by Work Project at no cost and is intended for use as is.\n" +
                        "\n" +
                        "This page is used to inform visitors regarding my policies with the collection, use, and disclosure of Personal Information if anyone decided to use my Service.\n" +
                        "\n" +
                        "If you choose to use my Service, then you agree to the collection and use of information in relation to this policy. The Personal Information that I collect is used for providing and improving the Service. I will not use or share your information with anyone except as described in this Privacy Policy.\n" +
                        "\n" +
                        "The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which are accessible at EventScape unless otherwise defined in this Privacy Policy.\n" +
                        "\n" +
                        "Information Collection and Use\n" +
                        "\n" +
                        "For a better experience, while using our Service, I may require you to provide us with certain personally identifiable information. The information that I request will be retained on your device and is not collected by me in any way.\n" +
                        "\n" +
                        "The app does use third-party services that may collect information used to identify you.\n" +
                        "\n" +
                        "Link to the privacy policy of third-party service providers used by the app\n" +
                        "\n" +
                        "Google Play Services\n" +
                        "Log Data\n" +
                        "\n" +
                        "I want to inform you that whenever you use my Service, in a case of an error in the app I collect data and information (through third-party products) on your phone called Log Data. This Log Data may include information such as your device Internet Protocol (“IP”) address, device name, operating system version, the configuration of the app when utilizing my Service, the time and date of your use of the Service, and other statistics.\n" +
                        "\n" +
                        "Cookies\n" +
                        "\n" +
                        "Cookies are files with a small amount of data that are commonly used as anonymous unique identifiers. These are sent to your browser from the websites that you visit and are stored on your device's internal memory.\n" +
                        "\n" +
                        "This Service does not use these “cookies” explicitly. However, the app may use third-party code and libraries that use “cookies” to collect information and improve their services. You have the option to either accept or refuse these cookies and know when a cookie is being sent to your device. If you choose to refuse our cookies, you may not be able to use some portions of this Service.\n" +
                        "\n" +
                        "Service Providers\n" +
                        "\n" +
                        "I may employ third-party companies and individuals due to the following reasons:\n" +
                        "\n" +
                        "To facilitate our Service;\n" +
                        "To provide the Service on our behalf;\n" +
                        "To perform Service-related services; or\n" +
                        "To assist us in analyzing how our Service is used.\n" +
                        "I want to inform users of this Service that these third parties have access to their Personal Information. The reason is to perform the tasks assigned to them on our behalf. However, they are obligated not to disclose or use the information for any other purpose.\n" +
                        "\n" +
                        "Security\n" +
                        "\n" +
                        "I value your trust in providing us your Personal Information, thus we are striving to use commercially acceptable means of protecting it. But remember that no method of transmission over the internet, or method of electronic storage is 100% secure and reliable, and I cannot guarantee its absolute security.\n" +
                        "\n" +
                        "Links to Other Sites\n" +
                        "\n" +
                        "This Service may contain links to other sites. If you click on a third-party link, you will be directed to that site. Note that these external sites are not operated by me. Therefore, I strongly advise you to review the Privacy Policy of these websites. I have no control over and assume no responsibility for the content, privacy policies, or practices of any third-party sites or services.\n" +
                        "\n" +
                        "Children’s Privacy\n" +
                        "\n" +
                        "These Services do not address anyone under the age of 13. I do not knowingly collect personally identifiable information from children under 13 years of age. In the case I discover that a child under 13 has provided me with personal information, I immediately delete this from our servers. If you are a parent or guardian and you are aware that your child has provided us with personal information, please contact me so that I will be able to do the necessary actions.\n" +
                        "\n" +
                        "Changes to This Privacy Policy\n" +
                        "\n" +
                        "I may update our Privacy Policy from time to time. Thus, you are advised to review this page periodically for any changes. I will notify you of any changes by posting the new Privacy Policy on this page.\n" +
                        "\n" +
                        "This policy is effective as of 2022-11-10\n" +
                        "\n" +
                        "Contact Us\n" +
                        "\n" +
                        "If you have any questions or suggestions about my Privacy Policy, do not hesitate to contact me at eventscape7@gmail.com.")
                .setCancelable(true)
                .setPositiveButton("Back", (dialogInterface, i) -> {}).show());

        back.setOnClickListener(v -> finish());
    }
}