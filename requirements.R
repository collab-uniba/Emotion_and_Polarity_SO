#check if caret is present
#if not, it will be installed
if(!require("caret", quietly = TRUE)) {
	install.packages(c("caret"), dependencies = c("Imports", "Depends","LinkingTo"), repos = "http://cran.mirror.garr.it/mirrors/CRAN/")
}

#check if LiblineaR is present
#if not, it will be installed
if(!require("LiblineaR", quietly = TRUE)) {
	install.packages(c("LiblineaR"), dependencies = c("Imports", "Depends","LinkingTo"), repos = "http://cran.mirror.garr.it/mirrors/CRAN/")
}

#check if e1071 is present
#if not, it will be installed
if(!require("e1071", quietly = TRUE)) {
	install.packages(c("e1071"), dependencies = c("Imports", "Depends","LinkingTo"), repos = "http://cran.mirror.garr.it/mirrors/CRAN/")
}

