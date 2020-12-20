## 환영합니다
이 플러그인으로 당신만의 미니게임을 처음부터 만들고 당신의 상상력으로 커스터마이징 할 수 있습니다.  
그러니까 이 플러그인은 미니게임 개발 도구라고 볼 수 있습니다.  
이 플러그인은 게임 플레이의 특정 부분에서 명령을 실행하여 작동합니다.  
그리고 미니게임이 돌아가는데에 주요한 메커니즘을 제공하고 여러가지 기능들이 있습니다. 
또한, 플러그인 기능 제안이나 오류 제보를 받습니다. 문의해주세요.

## 지원되는 기능
> 커스텀 GUi 메뉴, 커스텀 키트, 커스텀 월드보더, 커스텀 위치 저장 

> 메세지, 타이틀, 사운드 보내기 

> 미니게임 커맨드의 다양한 구문 지원

> 미니게임의 타이머 기능 지원

> 미니게임 데이타, 플레이어 데이타 미니게임 변수 지원

> 수학 연산 지원

> 그리고 더 많은 기능들 ...

## 다운로드 
##### [>> 다운로드링크 <<](https://github.com/FreedyPlugins/FreedyMinigameMaker/releases/latest/download/FreedyMinigameMaker.jar)

## 도움말
미니게임을 보다 쉽게 만들 수 있도록 도와주는 플러그인.
당신의 아이디어로 많은 것을 가능케 합니다. 
다양한 미니게임을 만들 수 있습니다. 스플리프, PVP, PVE 등등 
각 미니게임 이벤트(참여, 시작, 종료 반복 등등)들을 이용하여 미니게임 명령어들을 실행시킴으로써 미니게임이 돌아갑니다. 
미니게임 명령어에는 텔레포트, 인벤토리메뉴, 타이틀, 메세지, 지연되는 명령어 등이 있습니다. 
걱정마세요, 이 플러그인이 모든 것을 도와드릴 겁니다. 예시 구문을 한번 볼까요? 이것은 저의 config.yml 입니다.
```yaml
    conStartCmd:
    - fut {player} setData topic {random(전구, 지우개, 연필, 시계)}
    - fut {player} sendTitle game 20 60 20 주제는 {data(topic)} 입니다
```
해석하자면, 다음과 같습니다.
```yaml
    미니게임이시작됬을때콘솔에서명령어를실행한다:
    - fut 참여중인플레이어 데이타를저장한다 topic {이중에서랜덤으로고른다(전구, 지우개, 연필, 시계)}
    - fut 참여중인플레이어 타이틀을띄운다 미니게임에참여중인플레이어들에게 20페이드인 60유지시간 20페이드아웃 주제는 topic에 저장되어있는데이타 입니다
```

프리디미니게임메이커는 위와 같이 명령어 실행이 반복되서 하나의 미니게임을 만들 수 있게 해줍니다.

만약 당신이 헷갈린다면 제가 좀 더 설명해드리죠.

`{random(홍길동, 브루스, 프리디, 이종원)}`

위 구문을 해석하자면, 다음과 같습니다.

`홍길동과 브루스와 프리디와 이종원이 나열되어있는 리스트 중에 한개만 출력한다. `

`say 안녕하세요, {random(홍길동, 브루스, 프리디, 이종원)}님! 방가워요`

결과:

`[server] 안녕하세요, 이종원님! 방가워요`

이렇게 됩니다.

프리디미니게임메이커는 이런 명령어들의 반복입니다~.



예를 들어, 만약 미니게임이 플레이중이라면 플레이어가 움직이 전에 있었던 곳 바로 밑에 있는 블럭을 없애는 명령 구문입니다.

```yaml
/fut {player} if {isPlaying} == true fut {player} setBlock {world} {fromBlockX} {math(add, {fromBlockY}, -1)} {fromBlockZ} AIR
```  

또다시 예를 들어 보면, 만약 미니게임이 플레이중이지 않고, 플레이어가 5명이라면 게임이 시작되었다고 알리는 구문입니다.
```yaml
/fut {player} if {isPlaying} == false fut {player} if {playerSize} >= 5 fut {player} sendMsg game 게임이 시작되었습니다!
```  

네 예제는 많아요.

하지만 이제서 중요한 건 이 구문을 어떻게 쓰는 것인가 입니다.

미니게임 중에 많은 이벤트가 존재합니다!

플레이어가 미니게임에 참여할때, 퇴장할떄,

미니게임이 시작될 때, 미니게임이 끝날 때.

그리고 여러 이벤트를 직접 만드실 수 있어요!

미니게임이 실행 중일 때마다 구문이 작동되게 하는 반복이벤트도 있습니다!

음 좋아요! 미니게임을 생성해봅시다.

군말없이 알려드리죠.

 > 경고! 명령어의 대소문자를 지키세요.
 
 `/fmg set <게임이름> needClearInv true` 미니게임참여할 때 인벤토리를 비워야만한다면 이 명령어 입력

`/fmg create <게임이름> <최대인원> <시작인원> <시작대기초>` 미니게임 한개 생성

`/fmg set <게임이름> wait` 미니게임 입장시 위치 설정 - 안해도 됨

`/fmg set <게임이름> start` 미니게임 시작시 위치 설정 - 안해도 됨

`/fmg set <게임이름> end` 미니게임 종료시 위치 설정 - 안해도 됨

`/fmg set none kit create <킷이름>` 전투용 키트제작

`/fmg set none kit create empty` 미니게임 끝날 때 적용하는 인벤토리 키트 제작 (없다면 빈 인벤토리로 꼭 해야 함)

`/fmg set <게임이름> setCmd conStartCmd fut {player} sendTitle game 10 20 10 {color(&aFIGHT!)}`

`/fmg set <게임이름> setCmd conStartCmd fut {allPlayer} kit <킷이름>`

`/fmg set <게임이름> setCmd preDeathCmd fut {player} cancelEvent`

`/fmg set <게임이름> setCmd preDeathCmd fut {player} sendMsg game {player}이(가) 죽었습니다`

`/fmg set <게임이름> setCmd preDeathCmd fut {player} setPlayerData dead true`

`/fmg set <게임이름> setCmd preDeathCmd fut {player} do keepedWinner player, {player}`

`/fmg set <게임이름> setCmd keepedWinnerCmd fut {player} setData alivePlayers 0`

`/fmg set <게임이름> setCmd keepedWinnerCmd fut {player} executeConCmd fut {allPlayer} if {playerData(dead)} /= true fut {player} addData alivePlayers 1`

`/fmg set <게임이름> setCmd keepedWinnerCmd fut {player} executeConCmd fut {allPlayer} if {playerData(dead)} /= true fut {player} setData p {allPlayer}`

`/fmg set <게임이름> setCmd keepedWinnerCmd fut {player} if {integer({data(alivePlayers)})} == 1 fut {player} do stop player, {data(p)}`

`/fmg set <게임이름> setCmd stopCmd fut {player} sendMsg public {data(p)}이(가) {game}을 승리했습니다!`

`/fmg set <게임이름> setCmd stopCmd fut {player} shutDown`

`/fmg set <게임이름> setCmd quitCmd fut {player} kit empty`

이제 구문을 어떻게 사용하고 이벤트에 구문을 어떻게 적용하는지 알려드리겠습니다.



#### 조건 명령

**조건 명령은 데이터를 확인하고 다른 명령을 실행합니다.**

만약 값1과 값2가 같다면 명령어를 실행하는 명령 구문입니다.

`/fut <플레이어> if <값1> == <값2> <실행될명령>`  


만약 값1과 값2가 같지 않다면 명령어를 실행하는 명령 구문입니다.

`/fut <플레이어> if <값1> /= <값2> <실행될명령>` 


만약 값1이 값2보다 작다면 명령어를 실행하는 명령 구문입니다.

`/fut <플레이어> if <값1> < <값2> <실행될명령>` 


만약 값1이 값2보다 크다면 명령어를 실행하는 명령 구문입니다.

`/fut <플레이어> if <값1> > <값2> <실행될명령>` 

  
만약 값1이 값2보다 작거나 같으면 명령어를 실행하는 명령 구문입니다.

`/fut <플레이어> if <값1> <= <값2> <실행될명령>` 


만약 값1이 값2보다 크거나 같으면 명령어를 실행하는 명령 구문입니다.

`/fut <플레이어> if <값1> >= <값2> <실행될명령>` 


만약 값1과 값2가 같고 값3과 값4가 같다면 명령어를 실행하는 명령 구문입니다.
  

`/fut <플레이어> if <값1> == <값2> fut <플레이어> if <값3> == <값4> <실행될명령>`  
  

만약 값1과 값2가 같다면 명령1과 명령2를 실행하는 구문입니다
  

`/fut <플레이어> if <값1> == <값2> {do(<명령1> && <명령2>)}`  
  

만약 값1과 값2가 같다면 명령1과 명령2를 실행하고 값1과 값2가 같지 않다면 명령3과 명령4를 실행하는 구문입니다
  

`/fut <플레이어> if <값1> == <값2> {do(<명령1> && <명령2>)}{else(<명령1> && <명령2>)}`  

> 알림! {do()}나 {else()} 구문에서 여러 명령을 실행하려면 명렁과 명령 사이에 ' && ' (공백과 공백 사이에 '&&'를 넣은 문자열)을 넣어주세요

```yaml
fut {player} if true == true {do(fut {Player} sendMsg public 안녕하세요! && fut {player} sendMsg public 어서오세요!)}
```

>> 주의! {do()}나 {else()} 구문 속에 {add()}나 {data()} 같은 데이타 함수를 넣으면 안되요! fut <player> do player, <player> 실행 명령을 통해 데이타 함수를 사용하세요
  
***

#### 실행 명령

미니게임 유틸리티 명령어는 미니게임을 실행하는데 필요한 기능을 가지고 있습니다.

사용법을 확인하려면 콘솔에서 fut 명령어를 입력해보세요.


***

#### 데이타 함수


## 구문이란?
이 플러그인에는 많은 구문이 있습니다.
여기서 잠깐!
여기서 구문이란 무엇일까요.
구문은 `{playerName}` 이라고 쓰면 `플레이어 이름`이 출력되는 그런 것 입니다.

`{gameName}` 하면 미니게임 이름이 나오고,
`{playerAmount}` 하면 플레이어 수가 나오겠죠?

이제 예제를 한번 봅시다.

`/tp {player} 0 0 0`
이 명령을 실행했다고 가정합시다.
그러면 `{player}`자리에 `이 명령어를 실행한 플레이어의 이름'으로 교체 됩니다.
물론 `/tp` 명령이 실제로 그렇지는 않지만요.
하지만! 이 구문은 프리디미니게임메이커 플러그인에서는 작동합니다.
그치만! `/tp` 명령이 좀 더 복잡하게 되어 있어요. 한번 살펴볼까요?

`/fut {player} teleport private testGame start`

이렇게 되어 있네요..

제가 해석해보겠습니다.

`/fut {player} teleport private testGame start`
플레이어 티피한다 개인적으로 testGame 미니게임에 저장되어 있는 start 위치로

## 구문 리스트

### `{allPlayer}` 
미니게임에 참여중인 모든 플레이어 이름으로 대체합니다.
이 구문이 있는 명령어는 미니게임에 참여 중인 플레이어 수만큼 실행됩니다.

### `{onlinePlayer}` 
서버에 참여중인 모든 플레이어 이름으로 대체합니다.
이 구문이 있는 명령어는 서버에 참여 중인 플레이어 수만큼 실행됩니다.

### `{playerName}` 또는 `{player}` (둘다 같음)
이 명령어를 발동되게 한 원인의 플레이어의 이름으로 대체됩니다.

### `{playerIndex}`
이 명령어를 발동되게 한 원인의 플레이어의 참여중인 플레이어 목록 나열 번호로 대체합니다.

### `{maxPlayers}`
미니게임 입장 최대 인원을 대체합니다.

### `{randomPlayer}`
미니게임에 참여 중인 플레이어중 랜덤으로 하나를 뽑은 플레이어의 이름을 대체합니다.

### `{playerAmount}`
미니게임 시작 당시, 참여 중인 플레이어 수를 대체합니다.

### `{playerSize}`
미니게임에 참여 중인 플레이어의 수를 대체합니다.

### `{isPlaying}`
미니게임이 플레이 중인지 아닌지 여부를 대체합니다. 참이면 true, 거짓이면 false

### `{isWaiting}`
미니게임이 플레이를 위해 대기 중인지 아닌지 여부를 대체합니다. 참이면 true, 거짓이면 false

### `{game}` 또는 `{gameName}` (둘다 같음)
콘피그파일에 저장된 미니게임 이름을 대체합니다.

### `{gameType}`
미니게임의 타입을 대체합니다.

### `{math(add, 1, 2)}`
1 더하기 2 의 값을 대체합니다. add이외에 remainder, multiply, divide 이 있습니다

### `{playerData(testData)}`
따로 저장되어 있던 플레이어 데이타로 대체합니다.

### `{data(testData)}`
따로 저장되어 있던 미니게임 데이타로 대체합니다.

### `{miniGamedata(테스트, isPlaying)}`

테스트 미니게임이 시작되었는지 출력합니다. isPlaying 대신 playerList 로 미니게임에 참여중인 플레이어들을 모두 출력할 수도 있습니다. 그 외에는 따로 저장되어있던 미니게임 데이타로 대체합니다

### `{color(&a안녕하세요)}`
초록색 글씨로 대체합니다

### `{numeric(1004)}`
1004 라는 숫자가 숫자인지 아닌지를 대체합니다

### `{entityLoc(ce5e7f96-6c5d-43c1-a948-1c8c2388e47a)}`
Uuid의 엔티티의 위치를 대체합니다

### `{topLoc(world, 10, 5, -1)}`
위 좌표에 있는 블럭의 y좌표중에 가장 높은 위치에 있는 블럭좌표를 대체합니다

### `{contain(ㅁ, ㄱ, ㄴ, ㄷ, ㄹ, ㅁ)}`
ㄱ, ㄴ, ㄷ, ㄹ, ㅁ 중에서 ㅁ 이 있는지를 대체합니다

### `{sub(1, 3, 안녕하세요)}`
안녕하세요 문자열의 1 부터 3까지 잘라서 녕하세를 대체합니다

### `{blockName(world, 10, 5, -1)}`
위 좌표에 있는 블럭의 코드를 대체합니다 

### `{playerList(default)}`
미니게임에 있는 플레이어 이름을 모두 출력합니다. default가 아닌 all 이라면 모든 온라인 플레이어의이름을 모두 대체합니다.

### '{add(사, 일, 이, 삼)}'
일, 이, 삼에 사를 추가한 목록을 출력합니다.

### `{remove(일, 일, 이, 삼)}`
일, 이, 삼에 일을 제거한 목록을 출력합니다.

### `{file(playerPoint-ce5e7f96-6c5d-43c1-a948-1c8c2388e47a)}`
위 UUID 앞에 playerPoint가 붙여진 값을 data.yml 에서 찾아옵니다. 만약 그 값이 없다면 none이 출력됩니다. 문자와 문자 사이에 점(.)을 붙이면 앞에 있는 문자 속에 뒤에있는 문자가 저장됩니다. UUID만 점 앞에 붙이면 오류가 나므로 UUID앞에 아무 문자나 붙여야 합니다.

### `{date(<날짜포멧>)}`
https://nowonbun.tistory.com/502

### `{length(abcde)}`
abcde의 문자길이(5)를 출력합니다.

### `{indexOf(0, 10, 20, 30)}`
10, 20, 30 중에 0 번째에 오는 데이타를 대체합니다.

### `{valueOf(20, 10, 20, 30)}`
10, 20, 30 중에 20 데이타의 index를 대체해서 2를 대체합니다.

### `{sizeOf(일, 이, 삼, 사)}`
일, 이, 삼, 사의 목록의 갯수를 대체합니다.

### `{shuffle(1, 2, 3)}`
1, 2, 3의 나열순서를 섞어서 대체합니다.

### `{highList(1, 2, 3)}`
가장 큰 순서대로 나열한 3, 2, 1을 대체합니다.

### `{flip(3, 1, 2)}`
순서를 뒤집어 2, 1, 3을 대체합니다.

### `{lowestNumber(1, 2, 3)}`
가장 적은 숫자를 대체합니다.

### `{highestNumber(1, 2, 3)}`
가장 많은 숫자를 대체합니다.

### `{randomNumber(0, 9)}`
0부터 9까지 숫자 중에 하나를 골라 대체합니다.

### `{random(apple, bread, cheese)}`
apple, bread, cheese 충에 하나를 골라 대체합니다.

### `{constant(testMessage)}`
콘피그파일에 저장되어 있던 미니게임 상수로 대체합니다.

### `{playerTargetBlock(100)}`
플레이어가 100블럭 범위안에서 바라보고 있는 블럭의 위치를 대체합니다.

### `{itemAmount(1)}`
플레이어의 인벤토리에 1번째 칸 아이템 갯수를 출력합니다.

### `{itemType(1)}`
플레이어의 인벤토리에 1번째 칸 아이템 타입을 출력합니다.

### `{replace(안녕, 안녕하세요, 방가워, 잘지내, 안녕, 잘가, 친구들)}`
방가워, 잘지내, 안녕, 잘가, 친구들 중에 안녕을 안녕하세요로 모두 바꾼 목록을 대체합니다.

### `{integer(3.1415926535897932384626)}`
위 수의 소수점을 없앤 3을 대체합니다.

### `{abs(-7)}`
-7에 마이너스 부호가 있다면 제거한 값인 7을 대체합니다.

### `{cos(3)}`
3의 코사인값을 대체합니다.

### `{sin(3)}`
3의 사인 값을 대체합니다.

### `{tan(3)}`
3의 탄젠트 값을 대체합니다.

### `{round(3.5)}`
3.5를 반올림한 값인 4를 대체합니다.

### `{roundUp(3.5)}`
3.5를 올림한 값인 4를 대체합니다.

### `roundDown(3.5)}`
3.5를 내림한 값인 3을 대체합니다.



***


#### 이벤트 번들


**이벤트 번들은 어떤 상황에서 명령들을 실행합니다**


```yaml
blockBreakCmd:
- fut {player} sendMsg private no!
- fut {player} cancelEvent
```

`preJoinCmd`
플레이어가 미니게임에 참여하기 직전에 실행됩니다.

`joinCmd`
플레이어가 미니게임에 참여했을 때 실행됩니다.

`preQuitCmd`
플레이어가 미니게임을 퇴장하기 직전에 실행됩니다.

`quitCmd`
플레이어가 미니게임을 퇴장했을 때 실행됩니다.

`conStartCmd`
미니게임이 시작됬을 때 실행됩니다.

`preConEndCmd`
미니게임이 종료되기 직전에 실행됩니다.

`conEndCmd`
미니게임이 종료되고 실행됩니다.

`interactCmd` `{actionName} {action} {itemName} {itemDurability} {itemType}`
미니게임에 참여 중인 플레이어가 엔티티를 제외한 무언가를 클릭하거나 상호작용했을 때 실행됩니다.

`interactEntityCmd` `{entityName} {entityType} {itemName} {itemDurability} {itemType}`
미니게임에 참여 중인 플레이어가 엔티티를 클릭했을 때 실행됩니다.

`moveCmd:` `{fromBlockType} {fromBlockX} {fromBlockY} {fromBlockZ} {fromBlockFace} {fromBlockWorld} {toBlockType} {toBlockX} {toBlockY} {toBlockZ} {toBlockFace} {toBlockWorld}`
미니게임에 참여 중인 플레이어가 블럭 단위로 한 칸 움직였을 때 실행됩니다.

`blockBreakCmd` `{blockType} {blockX} {blockY} {blockZ} {blockFace}`
미니게임에 참여 중인 플레이어가 블럭을 부술 때 실행됩니다.

`blockPlaceCmd` `{blockType} {blockX} {blockY} {blockZ} {blockFace}`
미니게임에 참여 중인 플레이어가 블럭을 설치할 때 실행됩니다.

`blockDamageCmd` `{blockType} {blockX} {blockY} {blockZ} {blockFace}`
미니게임에 참여 중인 플레이어가 블럭을 때릴 때 실행됩니다.

`preDeathCmd` `{killerType} {killerName}`
미니게임에 참여 중인 플레이어가 죽기 직전에 실행됩니다.

`deathCmd` `{killer}`
미니게임에 참여 중인 플레이어가 죽고 실행됩니다.

`damagedCmd` `{entityName} {entityType} {itemName} {itemDurability} {itemType}`
미니게임에 참여 중인 플레이어가 어떤 엔티티에게 데미지를 줄 때 실행됩니다.

`damageCmd` `{cause}`
미니게임에 참여 중인 플레이어가 데미지를 입었을 때 실행됩니다.

`projectileCmd` `{cause}` `{damage}` `{projectileType}` `{projectileName}` `{projectileUuid}` `{entityName} {entityType}`
미니게임에 참여 중인 플레이어가 발사체로부터 데미지를 입었을 때 실행됩니다.

`dropCmd` `{itemName} {itemDurability} {itemType}`
미니게임에 참여 중인 플레이어가 아이템을 떨굴 때 실행됩니다.

`pickupCmd` `{itemName} {itemDurability} {itemType}`
미니게임에 참여 중인 플레이어가 아이템을 주울 때 실행됩니다.

`chatCmd` `{format} {chat}`
미니게임에 참여 중인 플레이어가 채팅을 칠 때 실행됩니다.

`commandCmd` `{command} {args}`
미니게임에 참여 중인 플레이어가 명령어를 칠 때 실행됩니다.

`worldChangeCmd` `{fromWorld}` `{toWorld}`
미니게임에 참여 중인 플레이어가 월드를 이동할 때 실행됩니다.

`vehicleDamageCmd` `{vehicleName}` `{vehicleType}`
미니게임에 참여 중인 플레이어가 보트나 카트에게 대미지를 줄 때 실행됩니다.

`vehicleExitCmd` `{vehicleName}` `{vehicleType}`
미니게임에 참여 중인 플레이어가 보트나 카트를 타고 있다가 내릴 때 실행됩니다.

`vehicleCollisionCmd` `{vehicleName}` `{vehicleType}`
미니게임에 참여 중인 플레이어가 타고 있는 보트나 카트가 엔티티와 충돌할 때 실행됩니다.

`명령번들이름Cmd` `{커스텀함수}`
`/fut <미니게임플레이어> do 명령번들이름 커스텀함수1, 값1, 커스텀함수2, 값2 ...`
do 실행 명령을 통해서 실행됩니다.

`keeped명령번들이름Cmd` `{커스텀함수}`
`/fut <미니게임플레이어> do keeped명령번들이름 커스텀함수1, 값1, 커스텀함수2, 값2 ...`
do 실행 명령을 통해서 실행됩니다 keeped번들은 데이타 함수가 대체되지 않습니다. 그래서 무의미한 /fut <player> if true == true 구문을 앞에 붙여서 데이타 함수를 대체해야 합니다.
이러한 keeped번들의 장점은 while 구문에서 데이타 함수를 매주기마다 새롭게 불러올 수 있고, 또 allPlayer 데이타 함수의 반복 구문에서 새롭게 데이타를 불러올 수 있습니다.

`메뉴이름ClickCmd` `{slot}`
어떤 메뉴이름의 GUI메뉴를 클릭했을 때 그 클릭한 위치 {slot}으로 명령번들을 실행합니다.



> 이 곳은 아직 완성되지 않았어요! 다음에 다시 찾아주세요..



## 개발자 API

## 디펜덴시
```xml
        <dependency>
            <groupId>Freedy</groupId>
            <artifactId>FreedyMinigameMaker</artifactId>
            <version>버전</version>
            <scope>system</scope>
        </dependency>
```


## 예시
```java
package freedy.learnspigot.events;

import freedy.freedyminigamemaker.FreedyMinigameMaker;
import freedy.freedyminigamemaker.MiniGame;
import freedy.freedyminigamemaker.MiniGames;
import freedy.learnspigot.LearnSpigot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatEvent implements CommandExecutor {

    LearnSpigot plugin;

    public ChatEvent(LearnSpigot plugin) {
        this.plugin = plugin;
    }


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                MiniGames miniGames = FreedyMinigameMaker.miniGames;
                if (miniGames.isJoined(player)) {
                    MiniGame miniGame = miniGames.getJoined(player);
                    for (Player p : miniGame.playerList) {
                        p.sendMessage("<" + p.getName() + "> " + args[0]);
                    }
                }
            }
        }

        return true;
    }
}
```
