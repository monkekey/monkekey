// pages/claner/claner.js
var util = require('../../utils/util.js');
const Moment = require('../../utils/Moment.js')
const clanerData = {
  data: {
    hasEmptyGrid: false,
    today: util.today(new Date),//for today
    curMonth: util.curMonth(new Date),//for today
    curYear: util.curYear(new Date),
    beginTime: Moment(new Date()).format('yyyy/MM/dd')
  },
  onLoad(options) {
    let beginDay = wx.getStorageSync("beginDay")
    let endTime = wx.getStorageSync("endDay")
    let curEndDate = '';
    let curYear = this.data.curYear;
    let curMonth = this.data.curMonth;
    let today = this.data.today;
    if (beginDay){
      if (Moment(beginDay).before(new Date(curYear, curMonth - 1, today))){
        beginDay = '';
        endTime = '';
      }  
    }
    if (endTime){
      var datearr1 = endTime.split("/")
      curEndDate = datearr1[1] + '月' + datearr1[2] + '日'
    }
    this.setData({ beginDay, endTime, curEndDate})
    this.initData();
  },
  initData(){
    var datearr2 = this.data.beginTime.split("/")
    const date = new Date();
    const cur_year = date.getFullYear();
    const cur_month = date.getMonth() + 1;
    const weeks_ch = ['日', '一', '二', '三', '四', '五', '六'];
    this.calculateEmptyGrids(cur_year, cur_month);
    this.calculateDays(cur_year, cur_month);
    this.setData({
      cur_year,
      cur_month,
      weeks_ch,
      curBigenDate: datearr2[1] + '月' + datearr2[2]+'日',
    })
  },
  //切换月
  handleCalendar(e) {
    const handle = e.currentTarget.dataset.handle;
    const cur_year = this.data.cur_year;
    const cur_month = this.data.cur_month;
    const curMonth = this.data.curMonth;
    if (handle === 'prev') {
      if (curMonth >= cur_month) {
        return;
      }
      let newMonth = cur_month - 1;
      let newYear = cur_year;
      if (newMonth < 1) {
        newYear = cur_year - 1;
        newMonth = 12;
      }

      this.calculateDays(newYear, newMonth);
      this.calculateEmptyGrids(newYear, newMonth);

      this.setData({
        cur_year: newYear,
        cur_month: newMonth
      })

    } else {
      let newMonth = cur_month + 1;
      let newYear = cur_year;
      if (newMonth > 12) {
        newYear = cur_year + 1;
        newMonth = 1;
      }

      this.calculateDays(newYear, newMonth);
      this.calculateEmptyGrids(newYear, newMonth);

      this.setData({
        cur_year: newYear,
        cur_month: newMonth
      })
    }
  },
  //获取当月1号是周几
  calculateEmptyGrids(year, month) {
    const firstDayOfWeek = new Date(Date.UTC(year, month - 1, 1)).getDay();
    let empytGrids = [];
    if (firstDayOfWeek > 0) {
      for (let i = 0; i < firstDayOfWeek; i++) {
        empytGrids.push(i);
      }
      this.setData({
        hasEmptyGrid: true,
        empytGrids
      });
    } else {
      this.setData({
        hasEmptyGrid: false,
        empytGrids: []
      });
    }
  },
  //获取当月天数
  calculateDays(year, month) {
    let days = [];
    let oldDays = this.data.oldDays || [];
    
    let beginTime = this.data.beginTime;
    let endTime = this.data.endTime;
    const thisMonthDays = new Date(year, month, 0).getDate();
    for (let i = 1; i <= thisMonthDays; i++) {
      var dateTime = Moment(new Date(year,month-1, i)).format('yyyy/MM/dd');
      var day = {
        day: i,
        dateTime: dateTime,
        selected:false,
        endTime:false,
      }
      
      let repeat = false;
      for (var j in oldDays){
        if (dateTime == oldDays[j].dateTime){
          repeat = true;
          day = oldDays[j]
        }
      }
      if (endTime){
        if (endTime == dateTime) {
          day.endTime = true;
        } else {
          day.endTime = false;
        }
        if (Moment(beginTime).before(dateTime) && Moment(endTime).after(dateTime)) {
          day.selected = true;
        } else {
          day.selected = false;
        }
      }
      days.push(day);
      if (!repeat){
        oldDays.push(day)
      }
    }
    // console.log('days',days)
    // console.log('oldDays', oldDays)
    this.setData({
      days, oldDays, endTime
    });
  },
  
  //选中日期
  changeDate(e){
    let item = e.currentTarget.dataset.item;
    let endTime = item.dateTime;
    this.chgDate(endTime)
  },
  chgDate(endTime){
    let days = this.data.days;
    let oldDays = this.data.oldDays;
    if (Moment(endTime).after(this.data.beginTime)){
      for (var i in oldDays){
        if (endTime == oldDays[i].dateTime){
          oldDays[i].endTime = true;
        }else{
          oldDays[i].endTime = false;
        }
        if (Moment(this.data.beginTime).before(oldDays[i].dateTime) && Moment(endTime).after(oldDays[i].dateTime)){
          oldDays[i].selected = true;
        }else{
          oldDays[i].selected = false;
        }
        for (var j in days){
          if (oldDays[i].dateTime == days[j].dateTime){
            days.splice(j, 1, oldDays[i])
          }
        }
      }
      var datearr1 = endTime.split("/")
      this.setData({ days, oldDays, endTime, curEndDate: datearr1[1] + '月' + datearr1[2]+'日'})
    }
  },
  onBill(){
    wx.setStorageSync("beginDay", this.data.beginTime)
    wx.setStorageSync("endDay", this.data.endTime)
    wx.navigateBack()// 返回上一页面
  },
  back(){
    wx.navigateBack()
  },
  clean(){
    this.setData({
      oldDays:[],
      endTime:'',
      curEndDate:''
    })
    this.initData()
  }

};

Page(Object.assign(clanerData,{}));
