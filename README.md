# # MyGallery

프로젝트를 진행하다보면 MediaStorage 에 접근하는 경우가 굉장히 많습니다.

미디어 디비에 정확한 이해없이 무분별한 copy and paste 가 반복되는 것을 막기위해 미디어 디비에 대해 공부하며 제작한 Custom Gallery 프로젝트입니다.



## # MediaStore ( 미디어 디비 )

* 안드로이드 시스템에서 제공하는 미디어 데이터 DB
* Provider를 이용해 미디어 파일을 가져올 수 있습니다.
* 시스템 내부적으로 mediastore 에 추가하여, 여러 어플리케이션에서 이용 할 수 있습니다.
* 실제로 sqlite DB로 구성되어 있으나, 공개된 API를 이용하여 쉽게 조작 할 수 있습니다.



## # Query

미디어 디비에서 query 는 데이터 요청문에 해당합니다. query의 유연한 조작으로 원하는 미디어 파일을 가져올 수 있습니다!! 다음은 Query parameter 입니다.

* URI
  * 어떤 데이터를 가져올지 설정합니다.
  * Images.Media.EXTERNAL_CONTENT_URI  — 이미지 파일
  * Video.Media.EXTERNAL_CONTENT_URI — 비디오 파일
  * Audio.Media.EXTERNAL_CONTENT_URI — 오디오 파일
  * MediaStore.Files.getContentUri(“external”) — 모든 파일
* Projection 
  * DB 내 원하는 column을 문자열 형식으로 지정할 수 있습니다.
  * null 로 설정하면 모든 column 을 불러옵니다.
  * ​
* Selection
  * Query에 조건을 지정합니다.
  * SQL 문의 조건절에 들어가는 문장을 그대로 사용할 수 있습니다.
* SelectionArgs
  * selection( 조건절 ) 에서 ' ? ' 로 설정한 문자를 순서대로 치환합니다.
* SortOrder
  * 정렬을 위해 사용됩니다.
  * ORDER BY 절로 사용되며 DESC( 내림차순 ), ASC( 오름차순 ) 등의 문자로 처리합니다.



[ 예시1 ] 예시를 살펴보겠습니다. 먼저 모든 이미지를 가져오는 경우입니다.

````java
public List<GalleryItem> getAllImagePathList() {
        List<GalleryItem> galleryItemList = new ArrayList<>();
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    	//URI - 가져올 데이터 타입을 설정합니다. 갤러리 앱이므로 이미지를 선택하였습니다.
        String[] projection = { 
             MediaStore.MediaColumns.DATA 
             ,MediaStore.MediaColumns.DISPLAY_NAME };
    	//PROJECTION - 어떠한 Column 을 가져올지 선택합니다.
    	//Image를 보여주기위해 DATA 와 경로를 표시하기위해 DISPLAY_NAME 을 가져옵니다. 
        String sortOrder = MediaStore.Images.Media._ID+ " DESC";
    	//SortOrder - 정렬의 기준을 정합니다. 
    	//생성순으로 정렬하기위해 _ID, 내림차순 DESC
    	Cursor cursor = mContext.getContentResolver()
            .query(uri, projection, null, null, sortOrder);

      	//Cursor 로 데이터를 탐색하는 부분은 아래에서 살펴보겠습니다.
        return galleryItemList;
    }
````

[ 예시2 ] 다음은 특정 날짜의 이미지를 가져오는 경우입니다.

````java
 public List<GalleryItem> getDateImagePathList(int startYear, int startMonth, int startDay,int endYear, int endMonth, int endDay) {
       	//Calendar 를 이용한 날짜 설정입니다 ( 생략 )
        List<GalleryItem> galleryItemList = new ArrayList<>();
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.MediaColumns.DATA
                ,MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        String selection = MediaStore.Images.Media.DATE_TAKEN + ">? and "
                + MediaStore.Images.Media.DATE_TAKEN + "<?";
     	//SELECTION - 특정 시간안에 있는 데이터만을 불러옵니다.
        String[] selectionArgs = {toDateVar.getTimeInMillis() + "", 
                         fromDateVar.getTimeInMillis() + "" };
     	//SELECTION_ARGS - selection 에 표기된 '?' 를 대체할 인자들입니다.
     	// SELECTION => 시작 시간 < DATA < 끝 시간, 
     	//*DATE_TAKEN 은 ms 단위이며 촬영 날짜를 나타냅니다. ( DATA_ADDED = 파일이 추가된 날짜(ms) )
     
        String sortOrder = MediaStore.Images.Media._ID+ " DESC";
      	
     	Cursor cursor = mContext.getContentResolver()
            .query(uri, projection, selection, selectionArgs, sortOrder);

     	//Cursor 로 데이터를 탐색하는 부분은 아래에서 살펴보겠습니다.
        return galleryItemList;
    }
````





## # Cursor

Cursor 는 쉽게 생각하면 포인터와 같습니다! 적절한 파라미터를 설정하여 Query 를 실행하였다면 원하는 결과를 얻게되겠죠? 결과로 만들어진 Table 의 어떠한 Row 를 가리키고 있는 것이 Cursor 입니다.

cursor 내 자주 사용되는 메소드를 알아보겠습니다.

* moveToFirst() — 커서를 가장 처음 위치로 옮깁니다.

* moveToLast() — 커서를 가장 마지막 위치로 옮깁니다.

* moveToNext() — 현재 위치의 다음 행으로 커서를 이동시킵니다.

  * 대부분 테이블 내의 여러 결과를 불러올 때 사용됩니다.
  * 단순히 이동하는 것에 멈추지 않고, 다음 행의 존재 여부를 boolean 타입으로 반환합니다.

* moveToPrevious() — 이전 행으로 커서 이동

* getColumnIndexOrThrow() — 특정 필드의 인덱스 값을 반환하며, 필드가 존재하지 않을 시에 예외를 발생시킵니다.

  ​

````java
public List<GalleryItem> getDateImagePathList(int startYear, int startMonth, int startDay,int endYear, int endMonth, int endDay) {
       	//위에서 다룬 Query 설정은 생략하겠습니다.
        Cursor cursor = mContext.getContentResolver()
            .query(uri, projection, selection, selectionArgs, sortOrder);
		
    	//moveToNext 는 다음 행 존재여부를 return함으로 조건절에 사용합니다.
        while (cursor.moveToNext()) {
            //cursor.getString(i) 여기서 i 는 projection 에서 설정한 컬럼의 순서입니다.
            // 0 = DATA,  1 = BUCKET_DISPLAY_NAME
            GalleryItem galleryItem = 
                new GalleryItem(cursor.getString(0)
                    , cursor.getString(1));
            galleryItemList.add(galleryItem);
        }
        cursor.close();
        return galleryItemList;
    }
````











