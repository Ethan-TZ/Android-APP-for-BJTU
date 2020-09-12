from http.cookiejar import CookieJar
import bs4
import requests
from java import jclass
import selenium
from selenium import webdriver
import re
import time, datetime

class Spider:
    session = requests.session()
    session.cookies=CookieJar()

    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36'
        ,'Referer':"https://cas.bjtu.edu.cn/auth/login/"
    }
    start_day = [2020, 8, 3]



    def __init__(self,loginname,password):
        self.grade = set()
        self.lesson = {}
        self.loginname = loginname
        self.password = password
        self.page = self.session.get("https://cas.bjtu.edu.cn/auth/login/", headers={
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36'})
        dic = {}
        lt = bs4.BeautifulSoup(self.page.text, 'html.parser')
        for line in lt.form.findAll('input'):
            if (line.attrs['name'] == 'loginname'):
                break
            dic[line.attrs['name']] = line.attrs['value']
        dic['loginname'] = loginname
        dic['password'] = password
        self.page = self.session.post("https://cas.bjtu.edu.cn/auth/login/",
                                      data=dic, headers=self.headers)

        # 得到日期 0:(year, num_week, week_day) 1: num_week to start
        self.dateinfo = (datetime.date.today().isocalendar(),
                         datetime.date.today().isocalendar()[1] - datetime.date(*self.start_day).isocalendar()[1] + 1)

    def getGrade(self):
        if (self.grade):
            return self.grade
        self._gotoUrl("https://mis.bjtu.edu.cn/module/module/10")
        self._gotoUrl(
            bs4.BeautifulSoup(self.page.text, 'html.parser').find(
                id='redirect'
            ).attrs['action']
        )
        self._gotoUrl('https://dean.bjtu.edu.cn/score/scores/stu/view/')

        self.grade = self.grade.union(set(self.get_page_grade(
            self.page
        )))

        datas = dict(page='1', perpage='500', ctype='ln')
        self._gotoUrl('https://dean.bjtu.edu.cn/score/scores/stu/view/', data=datas)
        self.grade = self.grade.union(set(self.get_page_grade(self.page)))
        return self.grade


    def get_page_grade(self,r):
        grade_selector = bs4.BeautifulSoup(self.page.text, 'html.parser')
        grade_set = []
        for i, grade_item in enumerate(grade_selector.findAll('tr')):
            if (len(grade_item.findAll('td')) != 8):
                continue
            buffer = []
            for j, ch in enumerate(grade_item.findAll('td')):
                if (j == 4):
                    st = ch.text
                    st = st.replace(' ', '').replace('\n', '')
                    buffer.append(st)
                    continue
                elif (j == 2):
                    st = ch.text
                    st = st.split('\n')
                    buffer.append(tuple([y for y in ((st[1] + st[2]).strip().split(' ')) if y != '']))
                    continue
                elif (j == 6 or j == 1 or j==3):
                    buffer.append(ch.text)
                    continue
            grade_set.append(tuple(buffer))
        return grade_set

    def getTheClass(self):
        self._gotoUrl("https://mis.bjtu.edu.cn/module/module/10")
        self._gotoUrl(
            bs4.BeautifulSoup(self.page.text, 'html.parser').find(
                id='redirect'
            ).attrs['action']
        )
        datas=dict(xnxq='2020-2021-1-2')
        self._gotoUrl('https://dean.bjtu.edu.cn/course_selection/courseselect/stuschedule/',data=datas)
        analyser=bs4.BeautifulSoup(self.page.text,'html.parser')
        class_={i:{j:"" for j in range(1,8)} for i in  range(1,8) }
        for i,lines in enumerate(analyser.find_all('tr')):
            for j,item in enumerate(lines.find_all('td')):
                if j>=1:
                    for child in item.children:
                        if child!='\n':
                            class_[i][j]+=str(child.text.replace(' ','').replace('\n',' '))
        return class_

    def _gotoUrl(self, next_url, data=None):
        self.headers['Referer'] = self.page.url
        if not data:
            self.page = self.session.get(next_url, headers=self.headers)
        else:
            next_url += '?'
            for i, keys in enumerate(data.keys()):
                if i != 0:
                    next_url += '&'
                next_url += keys + '=' + data[keys]
            self.page = self.session.get(next_url, headers=self.headers)

    def _Calc_GPA_AVG(self):
        self.getGrade()
        GPA=AVG=total=0
        for grade_item in self.grade:
            if(grade_item[3]=='P' or grade_item[3]=='F'):
                continue
            weight=float(grade_item[2])
            total+=weight
            if(grade_item[3].isdigit()):
                x=float(grade_item[3])
                AVG+=x*weight
                if(x >= 90):
                    GPA+=4.0*weight
                elif(x >= 85):
                    GPA+=3.7*weight
                elif (x >= 81):
                    GPA += 3.3*weight
                elif (x >= 78):
                    GPA += 3.0*weight
                elif (x >= 75):
                    GPA += 2.7*weight
                elif (x >= 72):
                    GPA += 2.3*weight
                elif (x >= 68):
                    GPA += 2.0*weight
                elif (x >= 65):
                    GPA += 1.7*weight
                elif (x >= 63):
                    GPA += 1.3*weight
                elif (x >= 60):
                    GPA += 1.0*weight
            else:
                x=grade_item[3]
                if (x == 'A'):
                    GPA += 4.0*weight
                    AVG += 90*weight
                elif (x == 'A-'):
                    GPA += 3.7*weight
                    AVG += 85*weight
                elif (x == 'B+'):
                    GPA += 3.3*weight
                    AVG += 81*weight
                elif (x == 'B'):
                    GPA += 3.0*weight
                    AVG += 78*weight
                elif (x == 'B-'):
                    GPA += 2.7*weight
                    AVG += 75*weight
                elif (x == 'C+'):
                    GPA += 2.3*weight
                    AVG += 72*weight
                elif (x == 'C'):
                    GPA += 2.0*weight
                    AVG += 68*weight
                elif (x == 'C-'):
                    GPA += 1.7*weight
                    AVG += 65*weight
                elif (x == 'D+'):
                    GPA += 1.3*weight
                    AVG += 63*weight
                elif (x == 'D'):
                    GPA += 1.0*weight
                    AVG +=60*weight
        GPA=round(GPA/total,2)
        AVG=round(AVG/total,2)
        return GPA,AVG


    # 空闲教室 获取今天的空闲教室信息
    # #fff 为空闲教室
    def getClassRoom(self):
        self._gotoUrl("https://mis.bjtu.edu.cn/module/module/10")
        self._gotoUrl(
            bs4.BeautifulSoup(self.page.text, 'html.parser').find(
                id='redirect'
            ).attrs['action']
        )
        weekday = "星期" + str(self.dateinfo[0][2])
        # 今天空闲教室信息
        information = {"第" + str(i) + "节": [] for i in range(1, 8)}
        # zc:教学周
        data = dict(page='1', zc=str(self.dateinfo[1]) , perpage='500')
        self._gotoUrl('https://dean.bjtu.edu.cn/classroom/timeholdresult/room_stat', data=data)
        analyer = bs4.BeautifulSoup(self.page.text, 'html.parser')
        for line in analyer.find_all('tr'):
            if len(line.find_all('td')) == 50:
                for i, item in enumerate(line.find_all('td')):
                    # print(item)
                    if i == 0:
                        room = item.text
                    elif item.attrs['title'].find(weekday) >= 0 and item.attrs['style'].find('#fff') >= 0:
                        information[item.attrs['title'][item.attrs['title'].find(weekday) + 4:]].append(room)
        #(第i节:教室列表)
        return information

    #杂项 获取
    def Others(self):
        #jjgq_ip:15天内过期的ip
        #ip_count:名下共有ip地址
        #net_fee:网费
        #ecard_year:一卡通余额
        self._gotoUrl('https://mis.bjtu.edu.cn/home/')
        self._gotoUrl('https://mis.bjtu.edu.cn/osys_ajax_wrap/')
        return [re.findall(re.compile(r'(?<=jjgq_ip\": \").+?(?=\")'),self.page.text)[0],
                re.findall(re.compile(r'(?<=ip_count\": \").+?(?=\")'),self.page.text)[0],
                re.findall(re.compile(r'(?<=net_fee\": \").+?(?=\")'),self.page.text)[0],
                re.findall(re.compile(r'(?<=ecard_yuer\": ).+?(?=,)'),self.page.text)[0],
                re.findall(re.compile(r'(?<=newmail_count\": \").+?(?=\")'),self.page.text)[0]
                ]

    # 查看邮件
    def getEmail(self):
        self._gotoUrl('https://mis.bjtu.edu.cn/home/')
        self._gotoUrl('https://mis.bjtu.edu.cn/module/module/26')
        str = self.page.text[self.page.text.find('<body>'):]
        datas = {}
        key_pattern = re.compile(r'(?<=name=\").+?(?=\")')
        keys = re.findall(key_pattern, str)
        value_pattern = re.compile(r'(?<=value=\").+?(?=\")')
        values = re.findall(value_pattern, str)
        for i in range(len(keys)):
            datas[keys[i]] = values[i].replace('@', '%40')

        self._gotoUrl("https://mail.bjtu.edu.cn/coremail/cmcu_addon/coremailsso.jsp", data=datas)
        sid = self.page.url[self.page.url.find('sid=') + 4:]
        datas = {
            'sid': sid,
            'func': 'mbox%3AlistMessages'
        }
        self._gotoUrl('https://mail.bjtu.edu.cn/coremail/s/json', data=datas)

        email_ids = re.findall(re.compile(r'(?<=\"id\":\").+?(?=\",\n)'), self.page.text)
        email_senders = re.findall(re.compile(r'(?<=\"from\":\").+?(?=,\n)'), self.page.text)
        email_subjects = re.findall(re.compile(r'(?<=\"subject\":\").+?(?=,)'), self.page.text)
        email_Date = re.findall(re.compile(r'(?<=\"sentDate\":\").+?(?=\",\n)'), self.page.text)
        email_isread = re.findall(re.compile(r'(?<=\"read\":).+?(?=,\n)'), self.page.text)

        email = []
        for i in range(len(email_ids)):
            if (email_senders[i].find(self.loginname) != -1):
                email_isread.insert(i, 'None')
            email.append((email_ids[i], email_senders[i].replace('\\', ''), email_subjects[i], email_Date[i],
                          email_isread[i].replace('}', '')))
        email.sort(key=lambda x: x[3], reverse=True)

        # 第一个参数获取邮件内容
        return email

    # https://mail.bjtu.edu.cn/coremail/XT5/jsp/viewMailHTML.jsp 一个mid(id) 确定邮件内容
    def _extract_from_mid(self, mid):
        self._gotoUrl('https://mail.bjtu.edu.cn/coremail/XT5/jsp/viewMailHTML.jsp', data={
            'mid': mid
        })
        try:
            return eval(repr(bs4.BeautifulSoup(
                re.findall(re.compile(r'(?<=var mainPartContent = \(\').+?(?=\'\))'),
                           self.page.text)[0], 'lxml').get_text()).replace('\\\\', '\\'))
        except:
            pass


def getgrade(loginname,password):
    GradeBean=jclass("com.example.studentmagicbox.Beans.GradeBean")
    net=Spider(loginname,password)
    result=net._Calc_GPA_AVG()
    jb=GradeBean(result[0],result[1])
    for x in net.getGrade():
        jb.add_grade(x[0],x[1][0],x[1][1],x[2],x[3],x[4])
    return jb


def getMail_and_Information(loginname,password):
    EmailBean=jclass("com.example.studentmagicbox.Beans.EmailBean")
    net=Spider(loginname,password)
    jb=EmailBean()
    for x in net.getEmail():
        jb.add(x[0],x[1],x[2],x[3])
    y=net.Others()
    jb.set(y[0],y[1],y[2],y[3],y[4])
    return jb

def getClassRoom(loginname,password):
    ClassroomBean=jclass("com.example.studentmagicbox.Beans.ClassroomBean")
    net=Spider(loginname,password)
    jb=ClassroomBean()
    datas=net.getClassRoom()
    jb.setTimeinfo(str(net.dateinfo))
    for keys in datas.keys():
       for values in datas[keys]:
         jb.add(int(keys[1]),values)
    return jb

def getSchedule(loginname,password):
    ScheduleBean=jclass("com.example.studentmagicbox.Beans.ScheduleBean")
    net=Spider(loginname,password)
    jb=ScheduleBean()
    datas=net.getTheClass()
    for i in datas.keys():
        for j in datas[i].keys():
            jb.set(j,i,datas[i][j])
    return jb