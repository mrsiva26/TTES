function sy = modify_f0(x,fs,val)

        [f0raw,ap]=exstraightsource(x,fs);
        disp(' initial max of f0raw is ');
        fprintf('%d ',max(f0raw));
        n3sgram = exstraightspec(x,f0raw,fs); 
        f0raw = f0raw'; 
        count_positive=0;
        sum0=0;
       if(val == 'a')
       ad=9.0000000e+001;
       else 
       ad=6.0000000e+001;    
       end
           
        for i= 1:length(f0raw)
            
        if(f0raw(i)> 0.0000000e+000 )
                
                sum0=sum0+f0raw(i);
                count_positive=count_positive+1;
                f0raw(i)=f0raw(i)+ ad;
            
        end
        
        end
        
        f0raw = f0raw';
        
        avge=sum0/count_positive;
        disp(' Size of f0raw is ');
        fprintf('%d ',size(f0raw));
        disp(' Size of CP is ');
        fprintf('%d ',count_positive);
        
        disp(' average of f0raw is ');
        fprintf(' is %d ',avge);
        disp(' final max of f0raw is ');
        fprintf('%d ',max(f0raw));
        sy = exstraightsynth(f0raw,n3sgram,ap,fs);
        soundsc(sy,fs);
        
        sy1 = sy./max(abs(sy(:)))*(1-(2^-(24-1)));
        wavwrite(sy1,fs,'emot.wav');
        
        
        
        