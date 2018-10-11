## Description

Small library allowing to connect multiple forms to single Progress Bar component representing the current progress/states of the forms. 


## Integration     

Add this in build.gradle file

```groovy
dependencies {
    compile 'com.gravity.customprogressbar:customprogressbar:0.1'
}
```

## Usage

-	In your layout add:

```xml
<com.gravity.customprogressbar.CustomProgressBar
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/custom_progress"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/blue"
        android:paddingBottom="30dp"
        android:paddingTop="30dp" />
```

- In your Activity add:

```kotlin

// 1st parameter specify the number of fields in a form 
var customProgressModel1 = CustomProgressModel(2, "1st form")
var customProgressModel2 = CustomProgressModel(2, "2nd form")

var formsModelList: List<CustomProgressModel> = ArrayList()
formsModelList += customProgressModel1
formsModelList += customProgressModel2

custom_progress.addProgressForms(this, formsModelList)

var focusChange = FocusChange()

//Total fields are 2 (in 1st form) + 2 (in 2nd form)
editText1.onFocusChangeListener = focusChange
editText2.onFocusChangeListener = focusChange
editText3.onFocusChangeListener = focusChange
editText4.onFocusChangeListener = focusChange

inner class FocusChange : View.OnFocusChangeListener {
  override fun onFocusChange(v: View, hasFocus: Boolean) {
    when (v.id) {
      R.id.editText1 -> if (hasFocus) {
        custom_progress.changeFormProgress(25)
      }
      R.id.editText2 -> if (hasFocus) {
        custom_progress.changeFormProgress(50)
      }
      R.id.editText3 -> if (hasFocus) {
        custom_progress.changeFormProgress(75)
      }
      R.id.editText4 -> if (hasFocus) {
        custom_progress.changeFormProgress(100)
      }
    }
  }
}
```

## License

```
Copyright 2018 Akash Verma

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
