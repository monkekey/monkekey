// 是否为空对象
function isEmptyObject(e) {
  var t;
  for (t in e)
    return !1;
  return !0
}

module.exports = {
  isEmptyObject: function (data) {
    isEmptyObject(data);
  },
}