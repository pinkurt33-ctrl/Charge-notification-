# ⚡ Charge Sound

Android app: charger lagate hi ek funny sound, nikalte hi dusra funny sound.

- Charger **lagane** pe → `charge_on.m4a` bajega
- Charger **nikalne** pe → `charge_off.mp3` bajega
- Sirf Android pe chalega (iPhone pe nahi)

---

## 📁 Folder structure

```
ChargeSound/
├── README.md
├── .github/workflows/build.yml     <- GitHub pe APK banata hai
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
└── app/
    ├── build.gradle.kts
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/pinku/chargesound/
        │   ├── MainActivity.kt
        │   ├── ChargeService.kt
        │   └── BootReceiver.kt
        └── res/raw/
            ├── charge_on.m4a       <- charger lagane ka sound
            └── charge_off.mp3      <- charger nikalne ka sound
```

---

## 🚀 APK kaise banaye (GitHub se, sabse easy)

1. github.com pe naya **repository** banao
2. Is folder ki **saari files aur folders** upload karo (**Add file -> Upload files**).
   `.github` folder zaroor upload karna, wahi APK banata hai
3. **Commit changes** dabao
4. Repo ke **Actions** tab me "Build APK" chalega (2-4 minute)
5. Green tick pe click karo -> neeche **Artifacts** se **ChargeSound-apk** download karo
6. Zip extract karo -> `app-debug.apk` phone me install karo
   (Settings me **Install unknown apps** allow karna padega)

> Internet sirf ek baar chahiye. APK banne ke baad app offline chalegi.

## 🛠 Android Studio se (optional)

1. Android Studio -> **Open** -> `ChargeSound` folder
2. Gradle sync hone do (pehli baar internet chahiye)
3. **Build -> Build APK(s)** ya phone connect karke Run dabao

---

## 📱 App kaise use kare

1. App kholo, **Notification permission Allow** karo
2. **Chalu karo** button dabao (ek chhoti notification dikhegi, ye zaroori hai)
3. Charger lagao / nikalo -> sound bajega
4. Bina charger ke check karna ho to **Test** buttons dabao

## 🔊 Sound kaise badle

`app/src/main/res/raw/` me files replace karo, naam **wahi** rakhna:

- `charge_on.m4a` (ya `.mp3`, par code me `R.raw.charge_on` hi use hota hai, extension koi bhi chalega)
- `charge_off.mp3`

Naam ke rules: sirf chhote letters, numbers aur `_`. Space, `-` ya bade letters nahi chalenge.

---

## ⚠️ Agar sound na bajey

- Phone silent ya media volume low to nahi
- Battery optimization: app ko **No restrictions** karo
- Xiaomi / Oppo / Vivo / Realme: **Autostart ON** karo
- Notification permission **Allow** honi chahiye
- App me **Chalu karo** ON hona chahiye

## ❌ Build fail ho jaye

GitHub Actions ke red cross wale run ka error log copy karke bhej do, fix kar denge.
