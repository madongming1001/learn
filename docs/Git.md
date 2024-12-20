## Git

#### Git 常用命令

- `git init`
- `git clone`
- `git remote add origin ***.git`
- `git push -u origin master`
- 推送到远程仓库的dev分支：`git push origin dev`
- `git log`
- `git log --graph --pretty=oneline --abbrev-commit`
- `git status`
- `git diff`
- `git add *`
- `git commit -m "message"`
- commit之后又改了一个小bug，但是又不想增加一个commit，可以用：`git commit --amend --no-edit`，直接将改动添加到上一次的commit中
- `git push`
- `git pull`
- `touch .gitignore`
- git commit -m '补交Oct18' --date "Mon Nov 29 18:18:18 2022 +0800"

####修改分支名称

1. 执行命令git checkout br_rename_old //切换到br_rename_old分支 （如果已经在这个分支下，可以不执行此步骤）
2. 执行命令git pull origin br_rename_old //将代码更新到和远程仓库一致
3. 执行命令git branch -m br_rename_old br_rename_new //将本地仓库的br_rename_old的名称修改为br_rename_new
4. 执行命令git push origin --delete br_rename_old //将远程分支br_rename_old删除
5. 执行命令git push --set-upstream origin br_rename_new //将本地分支push到远程仓库
6. git remote rename pb paul 重命名远程分支
7. git remote rm paul 删除远程分支

#### Git 标签管理

- 首先切换到需要打标签的分支上，然后使用`git tag v1.0`就可以在当前commit打上v1.0的标签
- `git tag v1.0 commitID` 对特定commit打标签
- 打标签时加上message：`git tag -a <tagname> -m "message"`
- `git tag` 查看所有标签
- `git show [tagname]` 查看标签详细信息
- `git push origin <tagname>`可以推送一个本地标签到远程仓库
- `git push origin --tags`可以推送全部未推送过的本地标签
- `git tag -d <tagname>`可以删除一个本地标签
- `git push origin :refs/tags/<tagname>`可以删除一个远程标签（先从本地删除）

#### Git 撤销与回滚

- **暂存区**：`git add`之后commit之前存在的区域；

- **工作区**：`git commit`之后存在的区域；

- **远程仓库**：`git push`之后；

- 作了修改，但还没`git add`，撤销到上一次提交：`git checkout -f -- filename`；`git checkout -f -- .`

- 作了修改，并且已经git add，但还没git commit：

  - 先将暂存区的修改撤销：`git reset HEAD filename`/`git reset HEAD`；此时修改只存在于工作区，变为了 "unstaged changes"；
  - 再利用上面的checkout命令从工作区撤销修改

- `git add`之后，作了修改，想丢弃这次修改：`git checkout -f --filename`会回到最近一次`git add`

- 作了修改，并且已经

  ```
  git commit
  ```

  了，想撤销这次的修改：

  - `git revert commitID`. 其实，`git revert`可以用来撤销任意一次的修改，不一定要是最近一次
  - `git reset --hard commitID`/`git reset --hard HEAD^`
    （HEAD表示当前版本，几个^表示倒数第几个版本，倒数第100个版本可以用HEAD~100）；参数`--hard`：强制将暂存区和工作区都同步到指定的版本
  - `git reset`和`git revert`的区别是：reset是用来回滚的，将HEAD的指针指向了想要回滚的版本，作为最新的版本，而后面的版本也都没有了；而revert只是用来撤销某一次更改，对之后的更改并没有影响
  - 然后再用`git push -f`提交到远程仓库

#### Git 分支管理

- 创建分支: `git branch test`

- 切换分支: `git checkout test`

- 创建并切换分支：`git checkout -b test`

- 将test分支的更改合并到master分支：先在test分支上commit、push，再：`git checkout master`; `git merge test`

- 如果合并时产生冲突：先手动解决冲突，再合并

- 删除分支：`git branch -d test`

- ```
  git stash
  ```

  - 如果当前分支还有任务没有做完，也不想提交，但此时需要切换或者创建其它分支，就可以使用stash将当前分支的所有修改（包括暂存区）先储藏起来；然后就可以切换到其它分支
  - 在其它分支工作完成之后，首先切换回原来的分支，然后使用`git stash list`命令查看
  - 可以使用`git stash apply <stash number>`恢复之前储藏的工作现场，再使用`git stash drop <stash number>`删除掉储藏的内容
  - 也可以直接用`git stash pop`恢复并删除内容

-

如果在其它分支上做了一个修改（比如修复了一个bug，这次修改有一个commitID），想要将这次修改应用到当前分支上，可以使用：`git cherry-pick commitID`
，可以复制一个特定的提交到当前分支

#### git reset --soft,--hard的区别

git reset 命令可以将当前的`HEAD`重置到特定的状态。
首先要搞清楚下面几个概念

- `HEAD`: `HEAD`就是指向当前分支当前版本的游标
- Index:
  Index即为暂存区，当你修改了你的git仓库里的一个文件时，这些变化一开始是unstaged状态，为了提交这些修改，你需要使用`git add`
  把它加入到index，使它成为staged状态。当你提交一个commit时，index里面的修改被提交。
- working tree: 即当前的工作目录。

##### **--soft**

使用`--soft`参数将会仅仅重置`HEAD`到制定的版本，不会修改index和working tree,而本地文件的内容并没有发生变化，*
*而index中仍然有最近一次提交的修改，这时执行git status会显示这些修改已经在暂存区中了**，无需再一次执行git add。修改已经在暂存区

##### --mixed

使用`--mixed`参数与--soft的不同之处在于，--mixed修改了index，使其与第二个版本匹配。index中给定commit之后的修改被unstaged。如果现在执行git
commit 将不会发生任何事，因为暂存区中没有修改，在提交之前需要再次执行git add。修改不再暂存区

##### --hard

使用`--hard`同时也会修改working tree，也就是当前的工作目录，如果我们执行`git reset --hard HEAD~`
，那么最后一次提交的修改，包括本地文件的修改都会被清除，彻底还原到上一次提交的状态且无法找回。所以在执行`reset --hard`
之前一定要小心

## git revert

##### 使用`git revert`也能起到回退版本的作用，不同之处在于

- `git revert <commit>`会回退到<commit>之前的那次提交，比如`git revert HEAD~3`会回退到最近的第4个提交的状态，而不是第3个
- `git revert`会产生一个新的commit，将这次回退作为一次修改记录提交，这样的好处是不修改历史提交记录。

**参考文章：**https://www.jianshu.com/p/c6927e80a01d

##Git每次clone都是同一项目问题
https://blog.csdn.net/liuxiao723846/article/details/83113317
因为git运行的时候会默认读取三个文件
Git的三个重要配置文件分别是/etc/gitconfig，${home}/.gitconfig，.git/config。这三个配置文件都是Git运行时所需要读取的，但是它们分别作用于不同的范围。
其中${home}/.gitconfig我默认设置了每次取得地址，所有每次读取都是同一个项目

- /etc/gitconfig: 系统范围内的配置文件，适用于系统所有的用户； 使用 git config 时， 加 --system 选项，Git将读写这个文件。
- ${home}/.gitconfig: 用户级的配置文件，只适用于当前用户； 使用 git config 时， 加 --global 选项，Git将读写这个文件。
- .git/config: Git项目级的配置文件，位于当前Git工作目录下，只适用于当前Git项目； 使用 git config 时，不加选项（ --system 和
  --global ），Git将读写这个文件。

####设置别名
git config --global alias.logp "log --pretty=oneline --abbrev-commit"

##隐藏敏感文件
在.gitignore文件添加想要隐藏的文件 然后git rm -r --cached 相对路径 然后 add commit push 远程就可以隐藏敏感文件了

##删除github历史敏感提交数据
git filter-branch -f --index-filter 'git rm -rf --cached --ignore-unmatch 文件相对路径' HEAD
git push origin --force --all`

####maven下载依赖源码
mvn dependency:resolve -Dclassifier=sources

###本地文件修改不追踪
git update-index --assume-unchanged ${文件路径}
###本地文件修改追踪
git update-index --no-assume-unchanged ${文件路径}



# git rebase

**功能1:**合并本地提交的多条commit

git rebase -i HEAD～commit条数量

**功能2:**合并分支

先在checkout上分支**(feature 1)**出来的分支上去 git rebase 原分支**(master)**，在切换到原分支**（master）**，git merge **feature 1** 这样在看主git分支的线就是一条直线，当把feature 1分支删除之后，先里面就不会有分支线的情况，看起来就像是在一个分支上修改的。解决了多个merge分支线条的情况。有一个清晰整洁的历史记录。