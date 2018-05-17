// 基础地址
const BASE_URL = 'http://127.0.0.1:9080/'
const WEBSOCKET_URL = 'ws://127.0.0.1:5544/ws'

// const BASE_URL = 'https://restapi.iyumi.com/'
// const WEBSOCKET_URL = 'wss://restapi.iyumi.com/ws'

// 接口地址
const API_URL = BASE_URL + 'hpd/'

// 静态资源
const STATIC_URL= BASE_URL + '/resource/'


// 分页大小
const PAGE_SIZE = 10

// 网站标题
const TITLE = '后台'

const EMOJI_CHAR = "😊-😋-😌-😍-😏-😚-😛-😝-😞-😟-😪-😬-😮-😺-😻-😼-😽-😾-😿-🙊-🙋-🙏-🚊-🚋-🚌-🍄-🍅-🍆-🍇-🍈-🍉-🍑-🍒-🍓-🐔-🐕-🐖-👦-👧-👨-👩-👰-👱-👲-👳-💃-💄-💅-💆-💇-💐-💑-💓-💘-😂-😃-😅-😆-😈-😒-😓-😔-😕-😖-😘-😙-😠-😡-😣-😤-😥-😧-😩-😳-😵-😷-🚁";

const EMOJI = [
              "0x1f60a","0x1f60b","0x1f60c","0x1f60d","0x1f60f","0x1f61a","0x1f61b","0x1f61d","0x1f61e","0x1f61f",
              "0x1f62a","0x1f62c","0x1f62e","0x1f63a","0x1f63b","0x1f63c","0x1f63d","0x1f63e","0x1f63f","0x1f64a",
              "0x1f64b","0x1f64f","0x1f68a","0x1f68b","0x1f68c","0x1f344","0x1f345","0x1f346","0x1f347","0x1f348",
              "0x1f349","0x1f351","0x1f352","0x1f353","0x1f414","0x1f415","0x1f416","0x1f466","0x1f467","0x1f468",
              "0x1f469","0x1f470","0x1f471","0x1f472","0x1f473","0x1f483","0x1f484","0x1f485","0x1f486","0x1f487",
              "0x1f490","0x1f491","0x1f493","0x1f498","0x1f602","0x1f603","0x1f605","0x1f606","0x1f608","0x1f612",
              "0x1f613","0x1f614","0x1f615","0x1f616","0x1f618","0x1f619","0x1f620","0x1f621","0x1f623","0x1f624",
              "0x1f625","0x1f627","0x1f629","0x1f633","0x1f635","0x1f637","0x1f681"
            ]

//"🚴".codePointAt(0).toString(16)   -->  1f6b4
//String.fromCodePoint("0x1f6b4")    -->  🚴

// var patt=/[\ud800-\udbff][\udc00-\udfff]/g;
// "😊".match(patt)

export default {
  BASE_URL,
  API_URL,
  STATIC_URL,
  WEBSOCKET_URL,
  PAGE_SIZE,
  EMOJI_CHAR,
  EMOJI
}
