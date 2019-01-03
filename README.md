# openrndr-boids
An implementation of the [Boids algorithm](https://en.wikipedia.org/wiki/Boids) using OpenRNDR, a Kotlin-based creative coding framework. This implementation is a damn-near straight port of the [Processing implementation](https://processing.org/examples/flocking.html) by [Daniel Shiffman](http://www.twitter.com/shiffman). 

If you press `spacebar`, the boids panic (which in this case means reversing their drive toward 'cohesion', as defined by the Boids algorithm). Press it again and they un-panic.   

![A screenshot showing the simple flocking algorithm in action](https://github.com/ryanbateman/openrndr-boids/blob/master/img/example.png)
