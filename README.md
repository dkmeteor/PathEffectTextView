#Screenshot

Please waiting for loading the gif...

![](/path1.gif)

![](/path2.gif)

![](/path3.gif)


#How to use

Step 1: add denpendence


    compile('com.dk.view.patheffect:Library:0.1.0@aar')
    
    
 if you are still using `Eclipse`, you can just copy source code or jar file to you project.


Step 2: add view yo your layout:

    <com.dk.view.patheffect.PathTextView
        android:id="@+id/path"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>

step 3: call `init` method like this:

    PathTextView mPathTextView = (PathTextView) findViewById(R.id.path);
	mPathTextView.init("Hello World");

Option settings:

    mPathTextView.setPaintType(PathTextView.Type.MULTIPLY);
    mPathTextView.setTextColor(color);
    mPathTextView.setTextSize(size);
    mPathTextView.setTextWeight(weight);

#NOTE

- Only Support capital letter, you can check this file for [`Path Data`](https://github.com/dkmeteor/PathEffectTextView/blob/master/Library/src/main/java/com/dk/view/patheffect/MatchPath.java), the data comes from [android-Ultra-Pull-To-Refresh](https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh/)

- the size unit is px , 72 means 72px*72px.

- the text weight unit is px.
 

#License

    Copyright 2015 Dean Ding

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

---
Developed By


Dean <93440331@qq.com>  

Weibo：http://weibo.com/u/2699012760

![](https://avatars0.githubusercontent.com/u/5019523?v=3&s=460)