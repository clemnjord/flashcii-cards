SET "ROOT=%~dp0\.."

pushd "%ROOT%\backend"
docker build --tag flashcii-cards-back .
popd

pushd "%ROOT%\frontend"
docker build --tag flashcii-cards-front .
popd