package spring.project.forum.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import spring.project.forum.model.Answer;
import spring.project.forum.model.Question;
import spring.project.forum.model.security.Authority;
import spring.project.forum.model.security.User;
import spring.project.forum.repository.AnswerRepository;
import spring.project.forum.repository.QuestionRepository;
import spring.project.forum.repository.security.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Component
//@Profile("h2")
public class H2Bootstrap implements CommandLineRunner {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public H2Bootstrap(QuestionRepository questionRepository, AnswerRepository answerRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() != 0 || questionRepository.count() != 0 || answerRepository.count() != 0)
            return;

        Authority userRoleAuthority = Authority.builder().name("ROLE_USER").build();
        Authority adminRoleAuthority = Authority.builder().name("ROLE_ADMIN").build();

        User u1 = User.builder()
                .username("user1")
                .authority(adminRoleAuthority)
                .password(passwordEncoder.encode("password"))
                .build();

        User u2 = User.builder()
                .username("user2")
                .authority(userRoleAuthority)
                .password(passwordEncoder.encode("password"))
                .build();

        User u3 = User.builder()
                .username("user3")
                .authority(userRoleAuthority)
                .password(passwordEncoder.encode("password"))
                .build();

        Question q1 = Question.builder()
                .title("Download problems after upgrading to Monterey")
                .content("After upgrading to Monterey I cannot save files using Safari or Brave Browser (it's browser on Chromium engine). Downloads are starting forever and never finish. I waited like an hour to test it.\n" +
                        "\n" +
                        "What is more, I found that I cannot save copy of image using Preview. I can press button but nothing happens.\n" +
                        "\n" +
                        "What is interesting, I can save files using Chrome or Firefox, browsers that I didn't use for a long time but have them installed.\n" +
                        "\n" +
                        "What might be the problem? Some kind of permission to folders problem?")
                .createdAt(LocalDate.now())
                .author(u1)
                .upVotes(List.of(u2, u3))
                .build();

        Answer a1 = Answer.builder()
                .content("The only solution I found is to reset the settings to the default download folder")
                .createdAt(LocalDate.now())
                .author(u2)
                .targetQuestion(q1)
                .upVotes(List.of(u1))
                .downVotes(List.of(u3))
                .build();

        Answer a2 = Answer.builder()
                .content("\n" +
                        "I don't know if you tried this: restart to make sure it is not a temporary system bug.\n" +
                        "\n" +
                        "I have had this \"saving image\" issue happened to me in a chatting software, where I could not save images directly via that app, but opening with Preview and then saving has always worked for me *Using the \"Export\" menu option, not \"Cmd + Save\"\n" +
                        "\n" +
                        "I am currently using Preview and it works fine, and the chatting software was updated and is working fine as well. Downloading works fine for me too...Assuming that it works well before the update, then access to the folder should not be a problem.\n" +
                        "\n" +
                        "Perhaps you forgot to click the pop up to \"allow download on this site\", which can be changed in Safari 's Websites setting.")
                .createdAt(LocalDate.now())
                .author(u3)
                .targetQuestion(q1)
                .upVotes(List.of(u1, u2))
                .downVotes(List.of(u3))
                .build();

        Answer a3 = Answer.builder()
                .content("What really helped was to set ownership of all things in my home folder to myself again, as suggested here: https://apple.stackexchange.com/a/51862/391874.")
                .createdAt(LocalDate.now())
                .author(u1)
                .targetQuestion(q1)
                .upVotes(List.of(u1))
                .build();

        q1.setAnswers(List.of(a1, a2, a3));

        Question q2 = Question.builder()
                .title("Mac unable to set rotations of multiple monitors")
                .content("Having a little problem with my MacbookPro 2019-15inch-i9 (Big Sur 2020.07.08). I have two vertical monitors(ASUS VG249 1080p 144Hz)(BenQ GW2270 1080p 60Hz) and one normal monitor(DELL 2718Q 4k) set up with it, and they were working fine with a \"H-shape\" setup.\n" +
                        "\n" +
                        "However I just found after I updated to Big Sur for a week, the setting is failed. I am able to set only 1 monitor rotated. I tried to set the second rotation after the first rotation is successful, but the this operation would reset all the settings of Display, which means the three monitors are all horizontal.\n" +
                        "\n" +
                        "Plus, I've known it can also be a problem to Mac OS 10.15 or 10.14, I've searched it and found these problem unsolved.\n" +
                        "\n" +
                        "Any help would be appreciated.?")
                .createdAt(LocalDate.now())
                .author(u1)
                .build();

        Answer a4 = Answer.builder()
                .content("I've solved this problem by making a safe mode reboot.\n" +
                        "\n" +
                        "You need to shut down your MacbookPro, hold command + R. After you get into the safe mode successfully, restart it again.\n" +
                        "\n" +
                        "This time you get into your Mac with a normal mode, and the dual rotation of monitors is working.\n" +
                        "\n" +
                        "A rough assumption is that a safe mode reboot may cleared some cache of the system?\n" +
                        "\n" +
                        "Hope my solution can be helpful to you.")
                .createdAt(LocalDate.now())
                .author(u1)
                .targetQuestion(q2)
                .build();

        q2.setAnswers(List.of(a4));

        Question q3 = Question.builder()
                .title("Spinning gray wheel in mac internet accounts when adding or modifying google logins")
                .content("I was getting the spinning gray wheel hang when adding or modifying google accounts")
                .createdAt(LocalDate.now())
                .author(u2)
                .build();

        Answer a5 = Answer.builder()
                .content("Setting Safari as my default browser allowed the login to pop up correctly")
                .createdAt(LocalDate.now())
                .author(u3)
                .targetQuestion(q3)
                .build();

        q3.setAnswers(List.of(a5));

        Question q4 = Question.builder()
                .title("What is the best simple setup for recording high quality audio on an iPad with the Explain Everything app?")
                .content("I am helping a friend out who makes educational YouTube videos on their iPad Pro (2018 model with USB-C) with the Explain Everything app. It's a whiteboarding app that allows you to make videos by whiteboarding and talking at the same time.\n" +
                        "\n" +
                        "By default, it records using the iPad microphone. If you plug in a headset with a USB-C to headphone adapter, it uses the microphone from the headset. This is what they are doing currently, with a relatively cheap $50 headset. The audio quality sounds ok, not great. But I'm not much of an audiophile, so it's hard for me to tell. I'm also worried about what kind of quality degradation there might be converting from a headphones jack to USB-C, even if they are using Apple's official adapter.\n" +
                        "\n" +
                        "Explain Everything has a support page where they suggest that for a professional setup, an external audio interface like a Focusrite Scarlett with a studio microphone and separate headset can be used. To me, this sounds like an overly complex setup. My friend likes just being able to take the iPad and headset and record from anywhere quiet.\n" +
                        "\n" +
                        "One crazy idea I have been toying with is getting them an AirPods Max headset. My hope is that with a high quality pair of headphones made by Apple paired with a tablet also made by Apple, any recorded audio is bound to be better than their current setup. Or at least audio cancellation would be better due to the three-mic setup of the Max. But I'm finding surprisingly little information online about AirPods Max recording quality.")
                .createdAt(LocalDate.now())
                .author(u2)
                .build();


//        Question q2 = Question.builder()
//                .title("example question title 2")
//                .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
//                .createdAt(LocalDate.now())
//                .closedAt(LocalDate.now())
//                .build();
//
//        Question q3 = Question.builder()
//                .title("example question title 3")
//                .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
//                .createdAt(LocalDate.now())
//                .closedAt(LocalDate.now())
//                .build();
//
//        Question q4 = Question.builder()
//                .title("example question title 4")
//                .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
//                .createdAt(LocalDate.now())
//                .build();
//
//        Question q5 = Question.builder()
//                .title("example question title 5")
//                .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
//                .createdAt(LocalDate.now())
//                .build();
//
//        Answer a3 = Answer.builder()
//                .content("a3Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem")
//                .targetQuestion(q1)
//                .createdAt(LocalDate.now())
//                .build();

//        q1.setAnswers(List.of(a1, a2));
//        q1.setBestAnswer(a2);
//        q2.setAnswers(List.of(a3));
//
//        a1.setTargetQuestion(q1);
//        a2.setTargetQuestion(q1);
//        a3.setTargetQuestion(q2);
//
//        q1.setAuthor(u1);
//        q1.setUpVotes(List.of(u1));
//        q2.setAuthor(u1);
//
//        a1.setAuthor(u1);
//        a2.setAuthor(u1);
//        a3.setAuthor(u1);
//
//        a3.setUpVotes(List.of(u1));

        questionRepository.saveAll(List.of(q1, q2, q3, q4));
        answerRepository.saveAll(List.of(a1, a2, a3, a4, a5));
//        userRepository.saveAll(List.of(u1, u2, u3));

        //questionRepository.saveAll(Set.of(q1, q2));
    }
}
