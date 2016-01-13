function y=voice_modify(a)
[x,fs]=wavread('op1.wav'); 
sound(x,fs);
subplot(1,2,1);
plot(x);


switch a
    case 'a'
        disp('angry')
        
        y=pvoc(x,1.09,1024);
        y2=modify_amplitude(y,fs,length(y));
        y3=modify_intensity(y2,6,fs);
        y4=modify_f0(y3,fs,a);
        
        subplot(1,2,2);
        plot(y4);

    case 'h'
        disp('happy');
        y=modify_f0(x,fs,7.000000e+001);
        subplot(1,2,2);
        plot(y);
    case 's'
        disp('sad')
        y=pvoc(x,0.5,512);
        y2=modify_intensity(y,0.05,fs);
        
        disp('sadd');
        sound(y2,16000);
        wavwrite(y2,16000,'emot.wav');
        
        subplot(1,2,2);
        plot(y2);
end