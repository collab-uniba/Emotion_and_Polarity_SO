#check if caret is present
#if not, it will be installed
if(!require("caret", quietly = TRUE)) {
	install.packages(c("caret"), dependencies = c("Imports", "Depends"), repos = "http://cran.mirror.garr.it/mirrors/CRAN/")
}

#check if LiblineaR is present
#if not, it will be installed
if(!require("LiblineaR", quietly = TRUE)) {
	install.packages(c("LiblineaR"), dependencies = c("Imports", "Depends"), repos = "http://cran.mirror.garr.it/mirrors/CRAN/")
}

